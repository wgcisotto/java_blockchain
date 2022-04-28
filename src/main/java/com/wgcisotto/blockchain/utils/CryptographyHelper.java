package com.wgcisotto.blockchain.utils;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {

    public static final String ECDSA = "ECDSA";
    public static final String BOUNCY_CASTLE = "BC";

    private CryptographyHelper(){

    }

    public static KeyPair ellipticCurveCrypto(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECDSA, BOUNCY_CASTLE);
            //SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //TODO: try to do the same as wallets when using seed words then create the appropriated constants
            //https://github.com/web3j/web3j/tree/master/crypto/src/main/java/org/web3j/crypto
            //https://github.com/bitcoinj/bitcoinj/blob/571f68895744d5f8e2bc44f9e1abe522af91fe8a/core/src/main/java/org/bitcoinj/wallet/DeterministicSeed.java#L254
            SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
            ECGenParameterSpec params = new ECGenParameterSpec("secp192k1");
            keyPairGenerator.initialize(params, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            //TODO: throw custom exception
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static byte[] applyECDSASignature(PrivateKey privateKey, String input) {
        Signature signature;
        byte[] strByte = input.getBytes();
        try {
            signature = Signature.getInstance(ECDSA, BOUNCY_CASTLE);
            signature.initSign(privateKey);
            signature.update(strByte);
            return signature.sign();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            //TODO: throw custom exception
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signature){
        try {
            Signature signatureVerifier = Signature.getInstance(ECDSA, BOUNCY_CASTLE);
            signatureVerifier.initVerify(publicKey);
            signatureVerifier.update(data.getBytes());
            return signatureVerifier.verify(signature);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | SignatureException e) {
            //TODO: throw custom exception
            e.printStackTrace();
            throw new RuntimeException();
        }


    }

}
