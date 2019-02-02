import contract.Voting;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private final static String PRIVATE_KEY = "d17744289f8ff62c76be3c4d8e94789a32bc5c2c9b2be666cf310b469dd9ef3e";
    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    private final static String RECIPIENT = "0x8d144FA2FFEF7c01ee8Aa823cA1bcED79eBe6e9D";
    private final static String CONTRACT_ADDRESS = "0x35bf496365b7e04454dbeb592fc876641d213614";
    private List<byte[]> candidateNames = new ArrayList<>();

    public static void main(String[] args) {
        try {
            new Main();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
     private Main() throws Exception {
        byte[] n1;
         byte[] n2;
         byte[] n3;
         n1 = asciiToHex("Rama");
         n2 = asciiToHex("Nick");
         n3 = asciiToHex("Jose");
        candidateNames.add(n1);
         candidateNames.add(n2);
         candidateNames.add(n3);
         Web3j web3 = Web3j.build(new HttpService());

         TransactionManager transactionManager = new RawTransactionManager(
                 web3,
                 getCredentialsFromPrivateKey()
                 );
         web3jClientVersion(web3);
        // String deployedAddress = deployContract(web3,transactionManager,candidateNames);
        Voting myContract = loadContract(CONTRACT_ADDRESS,web3,getCredentialsFromPrivateKey());
         System.out.println("Contract Address: "+myContract.getContractAddress());
       //  myContract.votesForCandidate(n1).send();
         RemoteCall<BigInteger> votes = myContract.totalVotesFor(n1);
         System.out.println("Vote For Rama: "+  votes.send());
    }
    private void web3jClientVersion(Web3j web3) throws IOException {
        Web3ClientVersion web3ClientVersion;

        web3ClientVersion = web3.web3ClientVersion().send();
        String web3ClientVersionString = web3ClientVersion.getWeb3ClientVersion();
        System.out.println("Web3 Client Version: " + web3ClientVersionString);
    }

    private Credentials getCredentialsFromPrivateKey(){
        return Credentials.create(PRIVATE_KEY);
    }
    private void transferEthereum(Web3j web3, TransactionManager transactionManager) throws Exception {

        Transfer transfer = new Transfer(web3, transactionManager);
        TransactionReceipt transactionReceipt = transfer.sendFunds(
                RECIPIENT,
                BigDecimal.ONE,
                Convert.Unit.ETHER,
                GAS_PRICE,
                GAS_LIMIT
        ).send();

    }
    private String deployContract(Web3j web3, TransactionManager transactionManager,List<byte[]> names) throws Exception {

        return Voting.deploy(web3,transactionManager,GAS_PRICE,GAS_LIMIT,names)
                .send()
                .getContractAddress();
    }

    private Voting loadContract(String CONTRACT_ADDRESS, Web3j web3, Credentials credentials){

        return Voting.load(CONTRACT_ADDRESS,web3,credentials,GAS_PRICE,GAS_LIMIT);
    }

    private static byte[] asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }
         String ss =  hex.toString() + String.join("", Collections.nCopies(32 - (hex.length()/2), "00"));
        return Numeric.hexStringToByteArray(ss);
    }
}
