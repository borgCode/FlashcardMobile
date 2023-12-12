package com.example.flashcardmobile.util;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.flashcardmobile.entity.Badge;
import com.example.flashcardmobile.entity.LearningAnalytics;
import com.example.flashcardmobile.repository.AnalyticsRepository;
import com.example.flashcardmobile.repository.BadgeRepository;
import com.example.flashcardmobile.repository.StudySessionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BadgeManager {

    private static BadgeManager instance = null;
    private final BadgeRepository badgeRepository;
    private final AnalyticsRepository analyticsRepository;
    private final StudySessionRepository studySessionRepository;
    private int cardsStudied;
    private int cardsAdded;
    private int cardsMastered;
    private int uniqueDays;

    public BadgeManager(Application application) {
        badgeRepository = new BadgeRepository(application);
        analyticsRepository = new AnalyticsRepository(application);
        studySessionRepository = new StudySessionRepository(application);
    }

    public static synchronized BadgeManager getInstance(Application application) {
        if (instance == null)
            instance = new BadgeManager(application);

        return instance;
    }

    public void initializeBadges() {

        badgeRepository.hasBadges().thenAccept(badgeCount -> {
            if (badgeCount == 0) {
                List<Badge> badges = Arrays.asList(
                        new Badge("New Learner", "study", "Study 100 cards", false, "cards_studied >= 100", 1),
                        new Badge("Intermediate Learner", "study", "Study 1000 cards", false, "cards_studied >= 1000", 2),
                        new Badge("Advanced Learner", "study", "Study 10000 cards", false, "cards_studied >= 10000", 3),
                        new Badge("Memory Apprentice", "master", "Master 50 cards", false, "cards_mastered >= 50", 1),
                        new Badge("Memory Maestro", "master", "Master 200 cards", false, "cards_mastered >= 200", 2),
                        new Badge("Memory Virtuoso", "master", "Master 500 cards", false, "cards_mastered >= 500", 3),
                        new Badge("Creator initiate", "add", "Add 100 cards", false, "cards_added >= 100", 1),
                        new Badge("Creator Scholar", "add", "Add 500 cards", false, "cards_added >= 500", 2),
                        new Badge("Creator Sage", "add", "Add 1000 cards", false, "cards_added >= 1000", 3),
                        new Badge("Study Novice", "days", "Study on 30 different days", false, "daysCount >= 30", 1),
                        new Badge("Study Adept", "days", "Study on 90 different days", false, "daysCount >= 90", 2),
                        new Badge("Study Guru", "days", "Study on 180 different days", false, "daysCount >= 180", 3)
                );
                for (Badge badge : badges) {
                    badgeRepository.insert(badge);
                }
            }
        });
    }

    public LiveData<List<Badge>> getAllBadges(LifecycleOwner lifecycleOwner) {
        getUserData();
        updateBadgeProgress(lifecycleOwner);
        return badgeRepository.getAllBadges();
    }

    public void updateBadgeProgress(LifecycleOwner lifecycleOwner) {
        

        badgeRepository.getAllBadges().observe(lifecycleOwner, badges -> {
            System.out.println("I am being observec");
            for (Badge badge : badges) {
                
                if (!badge.isAchieved() && checkIfCriteriaMet(badge)) {
                    
                    badge.setAchieved(true);
                    
                    badgeRepository.update(badge);
                }
            }
        });
        
    }

    private CompletableFuture<Void> getUserData() {
        CompletableFuture<Integer> sessionCountFuture = studySessionRepository.getSessionCount();
        CompletableFuture<List<LearningAnalytics>> analyticsFuture = analyticsRepository.getAllAnalytics();

        return CompletableFuture.allOf(sessionCountFuture, analyticsFuture)
                .thenRun(() -> {
                    this.uniqueDays = sessionCountFuture.join();
                    
                    List<LearningAnalytics> analytics = analyticsFuture.join();
                    for (LearningAnalytics analytic : analytics) {
                        this.cardsAdded += analytic.getCardsAdded();
                        this.cardsStudied += analytic.getCardsStudied();
                        this.cardsMastered += analytic.getCardsMastered();

                        
                    }
                });
    }

    private boolean checkIfCriteriaMet(Badge badge) {

        switch (badge.getName()) {
            case "New Learner":
                return cardsStudied >= 100;
            case "Intermediate Learner":
                return cardsStudied >= 1000;
            case "Advanced Learner":
                return cardsStudied >= 10000;
            case "Memory Apprentice":
                return cardsMastered >= 50;
            case "Memory Maestro":
                return cardsMastered >= 200;
            case "Memory Virtuoso":
                return cardsMastered >= 500;
            case "Creator initiate":
                return cardsAdded >= 100;
            case "Creator Scholar":
                return cardsAdded >= 500;
            case "Creator Sage":
                return cardsAdded >= 1000;
            case "Study Novice":
                return uniqueDays >= 30;
            case "Study Adept":
                return uniqueDays >= 90;
            case "Study Guru":
                return uniqueDays >= 180;
            default:
                return false;
        }
    }
}
    

