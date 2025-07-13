package ru.itis.vhsroni.semestrovka.protocol;

import ru.itis.vhsroni.semestrovka.exception.message.MessageTypeAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageType {

    public static final int CLIENT_TEMP_COUNT_INFO_REQUEST = 1;

    public static final int CLIENT_TEMP_COUNT_INFO_RESPONSE = 2;

    public static final int CLIENT_MAX_COUNT_INFO_REQUEST = 3;

    public static final int CLIENT_MAX_COUNT_INFO_RESPONSE = 4;

    public static final int GAME_WAS_STARTED_REQUEST = 5;

    public static final int GAME_WAS_STARTED_RESPONSE = 6;

    public static final int PLAYER_ID_ASSIGNED = 7;

    public static final int ROOM_NAME_INFO_REQUEST = 8;

    public static final int ROOM_NAME_INFO_RESPONSE = 9;

    public static final int PLAYER_POSITION_UPDATE_REQUEST = 10;

    public static final int PLAYER_POSITION_UPDATE_RESPONSE = 11;

    public static final int COOKIE_COLLECTED_REQUEST = 12;

    public static final int COOKIE_COLLECTED_RESPONSE = 13;

    public static final int PLAYER_JUMP_REQUEST = 14;

    public static final int PLAYER_JUMP_RESPONSE = 15;

    public static final int ALL_COOKIES_COLLECTED_REQUEST = 16;

    public static final int ALL_COOKIES_COLLECTED_RESPONSE = 17;

    public static final int SCORE_UPDATED_REQUEST = 18;

    public static final int SCORE_UPDATED_RESPONSE = 19;

    public static final int PLAYER_WANT_TO_COMPLETE_LEVEL = 20;

    public static final int PLAYER_COMPLETED_LEVEL = 21;

    public static final int ALL_PLAYERS_COMPLETED_LEVEL = 22;

    public static final int GAME_OVER = 23;

    public static final int NEW_LEVEL = 24;

    public static final int PLAYER_DISCONNECTED = 25;

    public static final int PORT_ALREADY_USED = 26;

    public static final int SERVER_FULL = 27;


    public static List<Integer> getAllTypes() {
        return Arrays.stream(MessageType.class.getFields()).map(field -> {
            try {
                return field.getInt(MessageType.class.getDeclaredConstructor());
            } catch (IllegalAccessException | NoSuchMethodException e) {
                throw new MessageTypeAccessException(e);
            }
        }).collect(Collectors.toList());
    }

    private MessageType(){}
}
