package com.wgcisotto.blockchain.model;

import com.wgcisotto.blockchain.constants.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
public class Miner {

    private Wallet wallet;

    public Miner(Wallet wallet){
        this.wallet = wallet;
    }

    @Getter
    private double reward;

    public void mine(Block block, BlockChain blockChain) {
       // createCoinBase(block);
        log.info("Start mining time: {}", LocalDateTime.now());
        while(!isGoldenHash(block)){
            block.incrementNonce();
            block.generateHash();
        }
        log.info("new block has just being mined.");
        log.info("Hash: {}",  block.getHash());
        log.info("End mining time: {}", LocalDateTime.now());
        blockChain.addBlock(block);
        reward += Constants.REWARD;
    }

    private void createCoinBase(Block block) {
        Transaction coinBaseTransaction = new Transaction(wallet.getPublicKey(), wallet.getPublicKey(), Constants.REWARD, Collections.singletonList(TransactionInput.builder().build()));
    }

    private boolean isGoldenHash(Block block) {
        String targetDifficult = new String(new char[Constants.DIFFICULT]).replace('\0', '0');
        return block.getHash().substring(0, Constants.DIFFICULT).equals(targetDifficult);
    }

}
