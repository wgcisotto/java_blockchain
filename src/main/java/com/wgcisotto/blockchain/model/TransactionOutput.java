package com.wgcisotto.blockchain.model;

import com.wgcisotto.blockchain.utils.SHA256Helper;
import lombok.Getter;

import java.security.PublicKey;

public class TransactionOutput {

    @Getter
    private String id;

    private String parentTransactionId;

    private PublicKey owner;

    @Getter
    private double amount;

    public TransactionOutput(PublicKey publicKey, double amount, String parentTransactionId){
        this.owner = publicKey;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        buildId();
    }

    private void buildId() {
        this.id = SHA256Helper.hash(owner.toString()+amount+parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey){
        return owner == publicKey;
    }


}
