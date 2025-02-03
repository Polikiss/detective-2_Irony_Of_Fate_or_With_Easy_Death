package itmo.polik;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
public class Bot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_USERNAME = "found_machine_bot";
    private static final Map<String, Character> morseCodeMap = new HashMap<>();
    private Boolean isStarted = false;

    static {
        morseCodeMap.put("._", 'А');
        morseCodeMap.put("_...", 'Б');
        morseCodeMap.put(".__", 'В');
        morseCodeMap.put("__.", 'Г');
        morseCodeMap.put("_..", 'Д');
        morseCodeMap.put(".", 'Е');
        morseCodeMap.put("..._", 'Ж');
        morseCodeMap.put("__..", 'З');
        morseCodeMap.put("..", 'И');
        morseCodeMap.put(".___", 'Й');
        morseCodeMap.put("_._", 'К');
        morseCodeMap.put("._..", 'Л');
        morseCodeMap.put("__", 'М');
        morseCodeMap.put("_.", 'Н');
        morseCodeMap.put("___", 'О');
        morseCodeMap.put(".__.", 'П');
        morseCodeMap.put("._.", 'Р');
        morseCodeMap.put("...", 'С');
        morseCodeMap.put("_", 'Т');
        morseCodeMap.put(".._", 'У');
        morseCodeMap.put(".._.", 'Ф');
        morseCodeMap.put("....", 'Х');
        morseCodeMap.put("_._.", 'Ц');
        morseCodeMap.put("___.", 'Ч');
        morseCodeMap.put("____", 'Ш');
        morseCodeMap.put("__._", 'Щ');
        morseCodeMap.put(".__._", 'Ъ');
        morseCodeMap.put("_.__", 'Ы');
        morseCodeMap.put("_.._", 'Ь');
        morseCodeMap.put(".._..", 'Э');
        morseCodeMap.put("..__", 'Ю');
        morseCodeMap.put("._._", 'Я');
    }

    public Bot() {
        Dotenv dotenv = Dotenv.load();
        this.BOT_TOKEN = dotenv.get("TELEGRAM_BOT_TOKEN");
        if (BOT_TOKEN == null || BOT_TOKEN.isEmpty()) {
            throw new RuntimeException("Telegram bot token is not set. Please create a .env file with TELEGRAM_BOT_TOKEN.");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.startsWith("/start")) {
                isStarted = true;
                sendMessage(chatId, "Введите 1 элемент");;
            } else if (isStarted) {
                String decodedMessage = decodeMorseCode(messageText);
                sendMessage(update.getMessage().getChatId(), decodedMessage);
            }
        }
    }

    private String decodeMorseCode(String morseCode) {
        String decodedMessage = "";
        if (morseCodeMap.containsKey(morseCode)) {
            decodedMessage += morseCodeMap.get(morseCode);
        } else {
            decodedMessage = "Невозвожно расшифровать";
        }
        return decodedMessage;
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "found_machine_bot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
