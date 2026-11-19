/*
 * Лабораторная работа № 1 по дисциплине «Информационная безопасность»
 * Тема: Реализация генератора паролей с заданными требованиями
 * Студент: Хайруллин Динар Айдарович
 * Группа: БПО09-24-02
 * Вариант: 22
 */
import java.security.SecureRandom;
import java.util.Scanner;

public class PasswordGenerator {

    // Длина пароля по варианту 22 (M = 8)
    private static final int PASSWORD_LENGTH = 8;

    // Множество специальных символов (коды ASCII 33..42): ! " # $ % & ' ( ) *
    private static final String SPECIAL_CHARS = "!\"#$%&'()*";

    // Малые буквы русского алфавита (32 буквы, согласно диапазонам кодов в методических указаниях)
    private static final String RUSSIAN_LOWER = "абвгдежзийклмнопрстуфхцчшщъыьэюя";

    // Криптографически стойкий генератор псевдослучайных чисел
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Генератор паролей (ЛР № 1, вариант 22) ===");
            System.out.println("Студент: Хайруллин Динар Айдарович, группа БПО09-24-02");

            while (true) {
                System.out.print("\nВведите идентификатор пользователя (или 'exit' для выхода): ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String userId = scanner.nextLine().trim();

                if (userId.equalsIgnoreCase("exit")) {
                    System.out.println("Работа программы завершена.");
                    break;
                }

                if (userId.isEmpty()) {
                    System.out.println("Ошибка: идентификатор не может быть пустым. Повторите ввод.");
                    continue;
                }

                int n = userId.length();
                int nm = n % 10;
                int n2Mod10 = (nm * nm) % 10;
                int n3Mod10 = (nm * nm * nm) % 10;
                int p = n2Mod10 + n3Mod10 + 1;

                String password = generatePassword(n);

                System.out.println("--------------------------------------------------");
                System.out.println("Идентификатор пользователя : " + userId);
                System.out.println("Длина идентификатора (N)   : " + n);
                System.out.println("P = N^2 mod 10 + N^3 mod 10 + 1 = "
                        + n2Mod10 + " + " + n3Mod10 + " + 1 = " + p);
                System.out.println("Сгенерированный пароль     : " + password);
                System.out.println("Длина пароля (M)           : " + password.length());
                System.out.println("Расшифровка позиций:");
                System.out.println("  b1..b3 (случайные цифры)                : " + password.substring(0, 3));
                System.out.println("  b4..b5 (случайные спецсимволы)          : " + password.substring(3, 5));
                System.out.println("  b6     (случайная малая лат. буква*)    : " + password.charAt(5));
                System.out.println("  b7     (случайная заглавная лат. буква) : " + password.charAt(6));
                System.out.println("  b8     (" + p + "-я малая русская буква)       : " + password.charAt(7));
                System.out.println("--------------------------------------------------");
            }
        }
    }

    /**
     * Генерирует пароль длиной 8 символов в соответствии с требованиями варианта 22.
     *
     * @param n длина идентификатора пользователя (N)
     * @return сгенерированный пароль b1b2b3b4b5b6b7b8
     */
    public static String generatePassword(int n) {
        char[] password = new char[PASSWORD_LENGTH];

        // 1. b1, b2, b3 — случайные цифры ('0'..'9', ASCII 48..57)
        for (int i = 0; i < 3; i++) {
            password[i] = (char) ('0' + RANDOM.nextInt(10));
        }

        // 2. b4, b5 — случайные символы из множества {!, ", #, $, %, &, ', (, ), *} (ASCII 33..42)
        password[3] = SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length()));
        password[4] = SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length()));

        // Примечание: в таблице 1 для варианта 22 позиция b6 не указана (опечатка в условии).
        // Для сохранения длины M = 8 и включения всех групп символов формируется случайная
        // малая буква английского алфавита ('a'..'z', ASCII 97..122).
        password[5] = (char) ('a' + RANDOM.nextInt(26));

        // 3. b7 — случайная заглавная буква английского алфавита ('A'..'Z', ASCII 65..90)
        password[6] = (char) ('A' + RANDOM.nextInt(26));

        // 4. b8 — P-ая по счёту малая буква русского алфавита,
        // где P = N^2 mod 10 + N^3 mod 10 + 1 (P принимает значения от 1 до 19)
        int nm = n % 10;
        int p = (nm * nm) % 10 + (nm * nm * nm) % 10 + 1;
        password[7] = RUSSIAN_LOWER.charAt(p - 1);

        return new String(password);
    }
}