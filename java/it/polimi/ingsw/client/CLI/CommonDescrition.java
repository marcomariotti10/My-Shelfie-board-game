package it.polimi.ingsw.client.CLI;

import java.io.Serializable;

public enum CommonDescrition implements Serializable {
        COMMON_A("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).\n" +
                  "The tiles of one group can be different from those of another group."),
        COMMON_B("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape).\n" +
                 "The tiles of one group can be different from those of another group."),
        COMMON_C("Four tiles of the same type in the four corners of the bookshelf."),
        COMMON_D("Two groups each containing 4 tiles of the same type in a 2x2 square.\n" +
                 "The tiles of one square can be different from those of the other square."),
        COMMON_E("Three columns each formed by 6 tiles of maximum three different types. One column can show the same or\n" +
                 "a different combination of another column."),
        COMMON_F("Eight tiles of the same type. There’s no restriction about the position of these tiles."),
        COMMON_G("Five tiles of the same type forming a diagonal."),
        COMMON_H("Four lines each formed by 5 tiles of maximum three different types. One line can show the same or\n" +
                 "a different combination of another line."),
        COMMON_I("Two columns each formed by 6 different types of tiles."),
        COMMON_J("Two lines each formed by 5 different types of tiles. One line can show the same or a different combination of the other line."),
        COMMON_K("Five tiles of the same type forming an X."),
        COMMON_L("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right,\n" +
                 "each next column must be made of exactly one more tile. Tiles can be of any type.");
    private final String desc;
    CommonDescrition(String desc) {this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
