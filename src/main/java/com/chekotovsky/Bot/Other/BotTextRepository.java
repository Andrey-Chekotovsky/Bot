package com.chekotovsky.Bot.Other;

import lombok.Getter;


public class BotTextRepository {
    @Getter
    static private final String botManual = """
            Charlie бот содержит следующие команды:
            Техничесикие:
            /manual - выдает пользователю весь список команд бота.
            /start - приветствующая команда, дающая краткое описание бота.
            Функциональные:
            /task - команда позволяющая послать сообщение в качестве задачи в опривязанные каналы. Чтобы сделать это ответьте данной командой на сообщение, которое необходимо сделать задачей.
            /linkGroup - позволяет привязать канал к чату. Чтобы это сделать нужно ответить данной командой на сообщение пересланное из необходимого канала.
            /unlinkGroup - позволяет отвязать канал от чата.
            /linkedGroups - позволяет узнать какие каналы в данный момент привязаны к данному чату.
            Настройки:
            /setClearPeriod - позволяет настроить период между чистками сообщений. В сообщении с командой через пробел после нее укажите раз в сколько минут необходимо чистить сообзения.
            /getClearPeriod - позволяет ухнать текузий период между чистками соообщений.
            """;
    @Getter
    static private final String noReplyToTaskMessage = "Мне необходимо, чтобы вы ответили на сообщение, которое обозначается как задача";
    @Getter
    static private final String wrongLinkGroupMessage = "Чтобы я мог присоеденить канал /linkGroup должен содержать ответ на сообщение пересланное из этого чата.";
    @Getter
    static private final String duplicateLinkGroupMessage = "Данный канал уже был присоединен к вашему чату";
    @Getter
    static private final String successfullLinkGroupMessage = "Канадл успешно присоединен к чату";
    @Getter
    static private final String successfullUnlinkGroupMessage = "Канадл был успешно отсоединен от вашего чата";
    @Getter
    static private final String chooseUnlinkChatsMessage = "Выберите каналы которые хотите открепить";
    @Getter
    static private final String startMessage = "Приветствую, мое имя Чарли. Я помогаю с рассылкой задач. Если необходимы подробности моей работы, используйте команду /manual";
    @Getter
    static private final String chooseChatsForTaskMessage = "Выберите каналы, в которые будет переслано задание";
    @Getter
    static private final String unlinkPrefix = "UNLINK_";
    @Getter
    static private final String sendPrefix = "SEND_";
    @Getter
    static private final String endCallback = "END";
    @Getter
    static private final String taskForAllCallback = "TASK_FOR_ALL";
    @Getter
    static private final String taskDoneCallback = "TASK_DONE";
    @Getter
    static private final String endButton = "Закончить";
    @Getter
    static private final String taskDoneButton = "Задача выполнена";
    @Getter
    static private final String sendAllButton = "Послать всем";
    @Getter
    static private final String taskSharingMessage = "Выберите каналы, которые должны получить сообщение. После нажмите \""+ endButton +"\" и сообщение  исчезнет";
    @Getter
    static private final String clearPeriodChangedMessage = "Пероиод между чистками сообщений был изменен";
    @Getter
    static private final String allGroupsMessage = "Все каналы присоединенные к чату:\n";
    @Getter
    static private final String userConfigSuccessfullyChangedMessage = "Все каналы присоединенные к чату:\n";
    static public String getClearPeriodMessage(long timeInMillis)
    {
        return "Текущий период между чистками сообщений " + timeInMillis / 1000 / 60 + " мин";
    }








}
