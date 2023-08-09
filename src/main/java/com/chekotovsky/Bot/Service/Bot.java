package com.chekotovsky.Bot.Service;

import com.chekotovsky.Bot.Config.BotConfig;
import com.chekotovsky.Bot.Dao.GroupIdDao;
import com.chekotovsky.Bot.Dao.MessageForDeleteDao;
import com.chekotovsky.Bot.Models.GroupId;
import com.chekotovsky.Bot.Models.MessageForDelete;
import com.chekotovsky.Bot.Other.BotTextRepository;
import com.chekotovsky.Bot.Other.TimerDependedBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final GroupIdDao groupIdDao;
    private final MessageForDeleteDao messageForDeleteDao;
    private static InlineKeyboardMarkup taskMarkup;
    static {
        taskMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton doneButton = new InlineKeyboardButton();
        doneButton.setText(BotTextRepository.getTaskDoneButton());
        doneButton.setCallbackData(BotTextRepository.getTaskDoneCallback());
        rowInline.add(doneButton);
        rowsInline.add(rowInline);
        taskMarkup.setKeyboard(rowsInline);
    }
    private static TimerDependedBoolean timerBoolean;
    static {
        timerBoolean = new TimerDependedBoolean(true);
        timerBoolean.setTimePeriodInMillis(100000L);
    }
    @Override
    public String getBotUsername() {
        return config.getName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(timerBoolean.getValue())
        {
            clear();
        }
        if (update.hasMessage() && update.getMessage().hasText())
        {
            processMessage(update);
        }
        else if (update.hasCallbackQuery()) {
            processCallBack(update);
        }
    }
    private void clear()
    {
        List<MessageForDelete> messages = messageForDeleteDao.selectAll();
        for (MessageForDelete message : messages)
        {
            deleteMessage(message.getChatId(), message.getMessageId());
        }
        messageForDeleteDao.deleteAll();
        timerBoolean.setValue(false);
    }
    private void processMessage(Update update)
    {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();
        switch (message)
        {
            case "/start":
                startRequest(chatId, update.getMessage().getChat().getFirstName());
                break;
            case "/task":
                if (update.getMessage().getReplyToMessage() == null)
                {
                    sendMessage(chatId, BotTextRepository.getNoReplyToTaskMessage());
                    break;
                }
                createTask(chatId, update.getMessage().getReplyToMessage().getMessageId());
                break;
            case "/linkGroup":
                if (update.getMessage().getReplyToMessage() == null ||
                        update.getMessage().getReplyToMessage().getForwardFromChat() == null)
                {
                    sendMessage(chatId, BotTextRepository.getWrongLinkGroupMessage());
                    break;
                }
                linkGroupRequest(chatId,
                        update.getMessage().getReplyToMessage().getForwardFromChat().getId(),
                        update.getMessage().getReplyToMessage().getForwardFromChat().getTitle());
                break;
            case "/unlinkGroup":
                createUnlinkMenu(chatId);
                break;
            case "/manual":
                sendMessage(chatId, BotTextRepository.getBotManual());
            default:
                break;
        }
    }
    private void processCallBack(Update update)
    {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        if (callbackData.equals(BotTextRepository.getTaskDoneCallback()))
        {
            editMessageMarkup(update.getCallbackQuery().getMessage().getChatId(),
                    callbackData, update.getCallbackQuery().getMessage().getReplyMarkup(),
                    update.getCallbackQuery().getMessage().getMessageId());
            messageForDeleteDao.insert(new MessageForDelete().toBuilder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .build());
        }
        else if (callbackData.equals(BotTextRepository.getEndCallback()))
        {

            deleteMessage(chatId, messageId);
        }
        else if (callbackData.equals(BotTextRepository.getTaskForAllCallback()))
        {

            sendToAllGroupsMessage(chatId,
                    update.getCallbackQuery().getMessage().getReplyToMessage().getText(), taskMarkup);
        }
        else if (callbackData.contains(BotTextRepository.getSendPrefix()))
        {
            sendRequest(callbackData,
                    update.getCallbackQuery().getMessage().getReplyToMessage().getText(),
                    update.getCallbackQuery().getMessage().getMessageId());
        }
        else if (callbackData.contains(BotTextRepository.getUnlinkPrefix()))
        {
            unlinkRequest(chatId, callbackData,
                    update.getCallbackQuery().getMessage().getMessageId(),
                    update.getCallbackQuery().getMessage().getReplyMarkup());
        }
    }

    private void sendRequest(String callbackData, String message, int messageId)
    {
        StringBuilder sb = new StringBuilder(callbackData);
        sb.delete(0, BotTextRepository.getSendPrefix().length());
        String groupId = sb.toString();
        sendMessage(Long.valueOf(groupId), message, taskMarkup);

    }
    private void unlinkRequest(long chatId, String callbackData, int messageId, InlineKeyboardMarkup keyboardMarkup)
    {
        StringBuilder sb = new StringBuilder(callbackData);
        sb.delete(0, BotTextRepository.getUnlinkPrefix().length());
        String groupId = sb.toString();
        groupIdDao.deleteByLinkedGroup(chatId, Long.valueOf(groupId));
        sendMessage(chatId, BotTextRepository.getSuccessfullUnlinkGroupMessage());
        System.out.println(callbackData);
        System.out.println(messageId);
        editMessageMarkup(chatId, callbackData, keyboardMarkup, messageId);
    }
    private void editMessageMarkup(long chatId, String callbackData, InlineKeyboardMarkup keyboardMarkup, int messageId)
    {
        EditMessageReplyMarkup messageCaption = new EditMessageReplyMarkup();
        InlineKeyboardMarkup newMarkup = cutFromMarkup(callbackData, keyboardMarkup);
        messageCaption.setReplyMarkup(newMarkup);
        messageCaption.setMessageId(messageId);
        messageCaption.setChatId(chatId);
        try {
            execute(messageCaption);
        }
        catch (TelegramApiException e)
        {

        }
    }
    private InlineKeyboardMarkup cutFromMarkup(String callbackData, InlineKeyboardMarkup keyboardMarkup)
    {
        List<List<InlineKeyboardButton>> rowsInline = keyboardMarkup.getKeyboard();
        for(int i = 0; i < rowsInline.size(); i++)
        {
            for(int j = 0; j < rowsInline.get(i).size(); j++) {
                if (rowsInline.get(i).get(j).getCallbackData().equals(callbackData))
                {
                    rowsInline.get(i).remove(j);
                    keyboardMarkup.setKeyboard(rowsInline);
                    return keyboardMarkup;
                }
            }
        }
        return keyboardMarkup;
    }
    private void startRequest(long chatId, String name)
    {
        sendMessage(chatId, BotTextRepository.getStartMessage());
    }
    private List<List<InlineKeyboardButton>> keyboardWithLinkedGroups(long chatID, String prefix)
    {
        List<GroupId> groupIds = groupIdDao.selectByChatId(chatID);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        for(int i = 0; i < groupIds.size(); i++)
        {
            button.setText(groupIds.get(i).getGroupName());
            button.setCallbackData(prefix + String.valueOf(groupIds.get(i).getGroupId()));
            rowInline.add(button);
            button = new InlineKeyboardButton();
            if (i % 2 == 1 || i == groupIds.size() - 1)
            {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
        }
        return rowsInline;
    }
    private void createUnlinkMenu(long chatID){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = keyboardWithLinkedGroups(chatID, BotTextRepository.getUnlinkPrefix());
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(BotTextRepository.getEndButton());
        button.setCallbackData(BotTextRepository.getEndCallback());
        rowInline.add(button);
        rowsInline.add(rowInline);
        markup.setKeyboard(rowsInline);
        sendMessage(chatID, BotTextRepository.getChooseUnlinkChatsMessage(), markup);
    }
    private void createTask(long chatID, int taskMessageId){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = keyboardWithLinkedGroups(chatID, BotTextRepository.getSendPrefix());
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(BotTextRepository.getSendAllButton());
        button.setCallbackData(BotTextRepository.getTaskForAllCallback());
        rowInline.add(button);
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(BotTextRepository.getEndButton());
        button.setCallbackData(BotTextRepository.getEndCallback());
        rowInline.add(button);
        rowsInline.add(rowInline);
        markup.setKeyboard(rowsInline);
        sendMessageWithReply(chatID, BotTextRepository.getChooseChatsForTaskMessage(), taskMessageId, markup);
    }
    private void linkGroupRequest(long chatId, long groupID, String groupName)
    {
        try {
            groupIdDao.insert(new GroupId().toBuilder()
                    .groupId(groupID)
                    .chatId(chatId)
                    .groupName(groupName)
                    .build());
            sendMessage(chatId, BotTextRepository.getSuccessfullLinkGroupMessage());
        }
        catch (DuplicateKeyException e)
        {
            sendMessage(chatId, BotTextRepository.getDuplicateLinkGroupMessage());
        }
    }
    private void sendMessage(long chatID, String massage)
    {
        SendMessage message = new SendMessage(String.valueOf(chatID), massage);
        try
        {
            execute(message);
        }
        catch (TelegramApiException ignored)
        {

        }
    }
    private void sendMessageWithReply(long chatID, String massage, int messageId)
    {
        SendMessage message = new SendMessage(String.valueOf(chatID), massage);
        message.setReplyToMessageId(messageId);

        try
        {
            execute(message);
        }
        catch (TelegramApiException ignored)
        {

        }
    }
    private void sendMessageWithReply(long chatID, String massage, int messageId, InlineKeyboardMarkup markup)
    {
        SendMessage message = new SendMessage(String.valueOf(chatID), massage);
        message.setReplyToMessageId(messageId);
        message.setReplyMarkup(markup);
        try
        {
            execute(message);
        }
        catch (TelegramApiException ignored)
        {

        }
    }
    private void sendToAllGroupsMessage(long chatID, String text, InlineKeyboardMarkup markup)
    {
        List<GroupId> groupIds = groupIdDao.selectByChatId(chatID);
        for(GroupId groupId : groupIds) {
            sendMessage(Long.valueOf(groupId.getGroupId()), text, markup);
        }
    }
    private void sendMessage(long chatID, String text, InlineKeyboardMarkup markup)
    {
        String group = String.valueOf(chatID);
        SendMessage message = new SendMessage(group, text);
        message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException ignored) {
        }
    }
    private void deleteMessage(long chatId, int messageId)
    {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        }
        catch (TelegramApiException e)
        {

        }
    }
}
