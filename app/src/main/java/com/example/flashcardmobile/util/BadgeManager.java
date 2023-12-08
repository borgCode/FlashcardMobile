package com.example.flashcardmobile.util;

import android.app.Application;
import com.example.flashcardmobile.entity.Badge;
import com.example.flashcardmobile.repository.AnalyticsRepository;
import com.example.flashcardmobile.repository.BadgeRepository;
import com.example.flashcardmobile.repository.StudySessionRepository;
import com.github.javafaker.App;

import java.util.Arrays;
import java.util.List;

public class BadgeManager {

    private static BadgeManager instance = null;
    private BadgeRepository badgeRepository;
    private AnalyticsRepository analyticsRepository;
    private StudySessionRepository studySessionRepository;

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
                        new Badge("New Learner", "Study 100 cards", false, "cards_studied >= 100"),
                        new Badge("Intermediate Learner", "Study 1000 cards", false, "cards_studied >= 1000"),
                        new Badge("Advanced Learner", "Study 10000 cards", false, "cards_studied >= 10000"),
                        new Badge("Memory Apprentice", "Master 50 cards", false, "cards_mastered >= 50"),
                        new Badge("Memory Maestro", "Master 200 cards", false, "cards_mastered >= 200"),
                        new Badge("Memory Virtuoso", "Master 500 cards", false, "cards_mastered >= 500"),
                        new Badge("Creator initiate", "Add 100 cards", false, "cards_added >= 100"),
                        new Badge("Creator Scholar", "Add 500 cards", false, "cards_added >= 500"),
                        new Badge("Creator Sage", "Add 1000 cards", false, "cards_added >= 1000"),
                        new Badge("Study Novice", "Study on 30 different days", false, "daysCount >= 30"),
                        new Badge("Study Adept", "Study on 90 different days", false, "daysCount >= 90"),
                        new Badge("Study Guru", "Study on 180 different days", false, "daysCount >= 180")
                );
                for (Badge badge : badges) {
                    badgeRepository.insert(badge);
                }
            }
        });
    }
}
    

