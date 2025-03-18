package io.github.dumijdev.jambaui.context.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BannerUtils {
    private static final String banner = """
                   __                __             __  ______
                  / /___ _____ ___  / /_  ____ _   / / / /  _/
             __  / / __ `/ __ `__ \\/ __ \\/ __ `/  / / / // / \s
            / /_/ / /_/ / / / / / / /_/ / /_/ /  / /_/ // /  \s
            \\____/\\__,_/_/ /_/ /_/_.___/\\__,_/   \\____/___/  \s
                                                    :: Jamba UI ::
            """;

    public static void printBanner() {
        try (var bannerResource = BannerUtils.class.getResourceAsStream("/banner.txt")) {
            if (bannerResource == null) {
                System.out.println(banner);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(bannerResource))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println(banner);
            }

        } catch (IOException e) {
            System.out.println(banner);
        }

    }
}
