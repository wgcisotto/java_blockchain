package com.wgcisotto.blockchain.model;

import java.util.ArrayList;
import java.util.List;

import com.wgcisotto.blockchain.constants.Constants;
import com.wgcisotto.blockchain.utils.CryptographyHelper;
import com.wgcisotto.blockchain.utils.SHA256Helper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@ToString
public class Block {

    private int id;
    @ToString.Exclude
    private int nonce;
    @ToString.Exclude
    private long timeStamp;
    @Getter @Setter
    private String hash;
    private String previousHash;
    @Getter @Setter
    private List<Transaction> transactions;

    public Block(String previousHash){
        //this.id = id;
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        generateHash();
    }

    public void incrementNonce() {
        this.nonce++;
    }

    public void generateHash() {
        String dataToHash = id + previousHash + timeStamp  + transactions + nonce;
        this.hash = SHA256Helper.hash(dataToHash);
    }

    public boolean addTransaction(Transaction transaction){
        if(transaction == null){
            return false;
        }

        if(!Constants.GENESIS_PREV_HASH.equals(previousHash)){
            if(!transaction.verifyTransaction()){
                log.warn("Transaction is not valid!");
                return false;
            }
        }
        transactions.add(transaction);

        log.info("Valid transaction={} added to the block", transaction.getTransactionId());

        return true;
    }

}
