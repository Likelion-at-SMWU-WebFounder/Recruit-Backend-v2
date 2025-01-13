package com.smlikelion.webfounder.admin.entity;

public enum Block {
    ISACTIVE("active"),
    ISBLOCK("block");

    private final String blockName;
    Block(String blockName) {
        this.blockName = blockName;
    }

    public String getBlockName() {
        return blockName;
    }

    public static Block getBlockByName(String blockName) {
        for (Block block : Block.values()) {
            if (block.blockName.equalsIgnoreCase(blockName)) {
                return block;
            }
        }
        return null;
    }
}
