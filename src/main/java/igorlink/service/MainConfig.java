package igorlink.service;

import igorlink.donationexecutor.DonationExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static igorlink.service.Utils.*;

public class MainConfig {
    private static final HashMap<String, HashMap<String, String>> donationAmountsHashMap = new HashMap<>();
    private static FileConfiguration config = DonationExecutor.getInstance().getConfig();
    public static int dirtAmount = 0;
    public static int diamondsAmount = 0;
    public static int breadAmount = 0;
    public static int bigBoomRadius = 0;
    public static String token;
    public static List<String> listOfBlackListedSubstrings = new ArrayList<>();
    public static List<String> listOfWhiteListedSubstrings = new ArrayList<>();
    private static Boolean twitchFilter;

    public MainConfig() {

    }

    //Геттер конфига
    public static FileConfiguration getConfig(){
        return config;
    }

    //Обновить данные из конфига
    public static void reloadMainConfig() {
        loadMainConfig(true);
    }

    //Загрузить данные из конфига без указания параметра перезагрузки
    public static void loadMainConfig() {
        loadMainConfig(false);
    }

    //Загрузка данных из конфигфайла с указанным параметром перезагрузки
    public static void loadMainConfig(Boolean isReload) {
        DonationExecutor.getInstance().saveDefaultConfig();

        //Если это перезагрузка, обновляем данные, очищаем список игроков
        if (isReload) {
            DonationExecutor.getInstance().reloadConfig();
            DonationExecutor.getInstance().listOfStreamerPlayers.clear();
        }

        config = DonationExecutor.getInstance().getConfig();

        @NotNull String stringWithPlayersNames = Objects.requireNonNull(config.getString("StreamersNamesList"));
        List <String> streamerPlayersNamesList = new ArrayList<>(Arrays.asList(stringWithPlayersNames.split(",")));
        for (String playerName : streamerPlayersNamesList) {
            DonationExecutor.getInstance().listOfStreamerPlayers.addStreamerPlayer(playerName);
        }

        logToConsole("При чтении файла конфигурации было добавлено §b" + DonationExecutor.getInstance().listOfStreamerPlayers.getNumberOfStreamers() + "§f стримеров.");

        dirtAmount = Integer.parseInt(Objects.requireNonNull(config.getString("DirtAmount")));
        diamondsAmount = Integer.parseInt(Objects.requireNonNull(config.getString("DiamondsAmount")));
        breadAmount = Integer.parseInt(Objects.requireNonNull(config.getString("BreadAmount")));
        bigBoomRadius = Integer.parseInt(Objects.requireNonNull(config.getString("BigBoomRadius")));

        token = config.getString("DonationAlertsToken");
        listOfBlackListedSubstrings = Arrays.asList(Objects.requireNonNull(config.getString("BlacklistedSubstrings")).split(","));
        listOfWhiteListedSubstrings = Arrays.asList(Objects.requireNonNull(config.getString("WhitelistedSubstrings")).split(","));

        if (Objects.equals(config.getString("TwitchFilter"), "true")) {
            twitchFilter = true;
        } else if (Objects.equals(config.getString("TwitchFilter"), "false")) {
            twitchFilter = false;
        }
        else {
            logToConsole("Ошибка при чтении значение TwitchFilter");
        }


    }



    public static void turnFilterOn() {
        twitchFilter = true;
        config.set("TwitchFilter", "true");
    }

    public static void turnFilterOff() {
        twitchFilter = false;
        config.set("TwitchFilter", "false");
    }

    public static Boolean getFilterStatus() {
        return twitchFilter;
    }

    public static HashMap<String, String> getNameAndExecution (@NotNull String donationAmount) {
        String thisDonateForStreamerName = null;
        String nameOfExecution = null;
        for (String p : donationAmountsHashMap.keySet()) {
            if (donationAmountsHashMap.get(p).containsKey(donationAmount)) {
                HashMap<String, String> temp = new HashMap<String, String>();
                thisDonateForStreamerName = p;
                nameOfExecution = donationAmountsHashMap.get(p).get(donationAmount);
                temp.put("name", p);
                temp.put("execution", nameOfExecution);
                return temp;
            }
        }

        return null;
    }


}
