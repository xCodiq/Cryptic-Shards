package me.xcodiq.crypticshards.utilities;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {

    /**
     * Get a color-formatted string using {@link ChatColor#translateAlternateColorCodes(char, String)}
     *
     * @param string The string you want to format
     * @return A formatted and colored string
     * @see ChatColor#translateAlternateColorCodes(char, String)
     */
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get a color-formatted List using {@link ChatColor#translateAlternateColorCodes(char, String)}
     *
     * @param list The list you want to format
     * @return A formatted and colored list
     * @see ChatUtils#format(String)
     */
    public static List<String> format(List<String> list) {
        return list.stream().map(ChatUtils::format).collect(Collectors.toList());
    }

    /**
     * Get a converted string
     *
     * @param integer the integer you want to convert
     * @return converted integer {@value "123456" -> "123k"}
     */
    public static String convertInt(int integer) {
        String n = Integer.toString(integer);
        int l = n.length();
        if (l < 4) return n;
        if (l < 7) {
            return decimal(n, 3) + "k";
        }
        if (l < 10) {
            return decimal(n, 6) + "M";
        }
        if (l < 13) {
            return decimal(n, 9) + "B";
        }
        if (l < 16) {
            return decimal(n, 12) + "T";
        }
        if (l < 19) {
            return decimal(n, 15) + "Q";
        }
        return n;
    }

    private static String decimal(String s, int i) {
        int b = s.length() - i;
        return s.substring(0, b) + "." + s.substring(b, b + 2);
    }
}
