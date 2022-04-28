package com.wgcisotto.blockchain;

import com.wgcisotto.blockchain.constants.Constants;
import com.wgcisotto.blockchain.model.*;

import java.security.Security;

public class App {

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        BlockChain chain = new BlockChain();

        Wallet userA = new Wallet();
        Wallet userB = new Wallet();
        Wallet userC = new Wallet();
        Wallet minerWallet = new Wallet();
        Miner miner = new Miner(minerWallet);

        //create genesis transaction that sends 500 coins to userA:
        Transaction genesisTransaction = new Transaction(userC.getPublicKey(), userA.getPublicKey(), 500, null);
        genesisTransaction.sign(userC.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getReceiver(), genesisTransaction.getAmount(), genesisTransaction.getTransactionId()));

        BlockChain.UTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));

        System.out.println("Constructing the genesis block...");
        Block genesis = new Block(Constants.GENESIS_PREV_HASH);
        genesis.addTransaction(genesisTransaction);
        miner.mine(genesis,chain);

        Block block1 = new Block(genesis.getHash());
        System.out.println("\nuserA's balance is: " + userA.balance());
        System.out.println("\nuserA tries to send money (120 coins) to userB...");
        block1.addTransaction(userA.transfer(userB.getPublicKey(), 120).orElse(null));
        miner.mine(block1,chain);
        System.out.println("\nuserA's balance is: " + userA.balance());
        System.out.println("userB's balance is: " + userB.balance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nuserA sends more funds (600) than it has...");
        block2.addTransaction(userA.transfer(userB.getPublicKey(), 600).orElse(null));
        miner.mine(block2,chain);
        System.out.println("\nuserA's balance is: " + userA.balance());
        System.out.println("userB's balance is: " + userB.balance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nuserB is attempting to send funds (110) to userA...");
        block3.addTransaction(userB.transfer( userA.getPublicKey(), 110).orElse(null));
        System.out.println("\nuserA's balance is: " + userA.balance());
        System.out.println("userB's balance is: " + userB.balance());
        miner.mine(block3,chain);

        System.out.println("Miner's reward: "+miner.getReward());

    }
}
