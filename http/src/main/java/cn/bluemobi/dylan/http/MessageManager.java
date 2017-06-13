package cn.bluemobi.dylan.http;

/**
 * 用户消息管理类
 * Created by yuandl on 2017-03-31.
 */

public class MessageManager {
    private static volatile MessageManager messageManager = null;


    private MessageManager() {
    }

    public static MessageManager getMessageManager() {
        if (messageManager == null) {
            synchronized (MessageManager.class) {
                if (messageManager == null) {
                    messageManager = new MessageManager();
                }
            }
        }
        return messageManager;
    }

    /**
     * 默认使用中文
     */
    private boolean useEnglishLanguage = false;
    /**
     * 默认在其他状态的时候给用户提醒响应的错误信息
     */
    private MessageModel showMessageModel = MessageModel.OTHER_STATUS;

    /**
     * 设置提示中英文
     *
     * @param useEnglishLanguage
     */
    public MessageManager setUseEnglishLanguage(boolean useEnglishLanguage) {
        this.useEnglishLanguage = useEnglishLanguage;
        return messageManager;
    }

    /**
     * 获取是否使用英文
     *
     * @return
     */
    public boolean isUseEnglishLanguage() {
        return useEnglishLanguage;
    }

    /**
     * 获取消息提醒模式
     *
     * @return
     */
    public MessageModel getShowMessageModel() {
        return showMessageModel;
    }

    /**
     * 设置给用户提醒消息的模式
     *
     * @param messageModel
     */
    public MessageManager setShowMessageModel(MessageModel messageModel) {
        this.showMessageModel = messageModel;
        return messageManager;
    }

    /**
     * 用户提醒消息的模式
     */
    public enum MessageModel {
        All, OTHER_STATUS, NO
    }

}
