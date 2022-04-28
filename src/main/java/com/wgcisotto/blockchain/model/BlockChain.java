package com.wgcisotto.blockchain.model;

import java.util.*;
import java.util.stream.Collectors;

public class BlockChain {

    private List<Block> blockChain;

    public static Map<String, TransactionOutput> UTXOs;

    public BlockChain(){
        this.UTXOs = new HashMap<>();
        this.blockChain = new ArrayList<>();
    }

    public void addBlock(Block block){
        blockChain.add(block);
    }

    public List<Block> getBlockChain() {
        return this.blockChain;
    }

    public int getSize(){
        return this.blockChain.size();
    }

    @Override
    public String toString() {
        return this.blockChain.stream()
                .map(Block::toString)
                .collect(Collectors.joining("\n"));
    }

}