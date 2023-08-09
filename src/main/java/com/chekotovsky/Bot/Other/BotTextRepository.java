package com.chekotovsky.Bot.Other;

import lombok.Getter;


public class BotTextRepository {
    @Getter
    static private String botManual = """
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
            
            """;
    @Getter
    static private String noReplyToTaskMessage = "Мне необходимо, чтобы вы ответили на сообщение, которое обозначается как задача";
    @Getter
    static private String wrongLinkGroupMessage = "Чтобы я мог присоеденить канал /linkGroup должен содержать ответ на сообщение пересланное из этого чата.";
    @Getter
    static private String duplicateLinkGroupMessage = "Данный канал уже был присоединен к вашему чату";
    @Getter
    static private String successfullLinkGroupMessage = "Канадл успешно присоединен к чату";
    @Getter
    static private String successfullUnlinkGroupMessage = "Канадл был успешно отсоединен от вашего чата";
    @Getter
    static private String chooseUnlinkChatsMessage = "Выберите каналы которые хотите открепить";
    @Getter
    static private String startMessage = "Приветствую, мое имя Чарли. Я помогаю с рассылкой задач. Если необходимы подробности моей работы, используйте команду /manual";
    @Getter
    static private String chooseChatsForTaskMessage = "Выберите каналы, в которые будет переслано задание";
    @Getter
    static private String unlinkPrefix = "UNLINK_";
    @Getter
    static private String sendPrefix = "SEND_";
    @Getter
    static private String endCallback = "END";
    @Getter
    static private String taskForAllCallback = "TASK_FOR_ALL";
    @Getter
    static private String taskDoneCallback = "TASK_DONE";
    @Getter
    static private String endButton = "Закончить";
    @Getter
    static private String taskDoneButton = "Задача выполнена";
    @Getter
    static private String sendAllButton = "Послать всем";
    @Getter
    static private String taskSharingMessage = "Выберите каналы, которые должны получить сообщение. После нажмите \""+ endButton +"\" и сообщение  исчезнет";









}
