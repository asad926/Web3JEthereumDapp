package javafx.client;

import com.sun.org.apache.xml.internal.serializer.Serializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import web3j.ethereum.contract.Voting;
import web3j.ethereum.contract.Web3jWallet;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationFXMLController implements Initializable{

    @FXML
    private Button btn_vote;

    @FXML
    private Text vote_c1;

    @FXML
    private Text vote_c2;

    @FXML
    private Text vote_c3;

    @FXML
    private Text vote_c4;

    @FXML
    private RadioButton cand_c1;

    @FXML
    private ToggleGroup candidates;

    @FXML
    private RadioButton cand_c4;

    @FXML
    private RadioButton cand_c3;

    @FXML
    private RadioButton cand_c2;

    @FXML
    private Label address;

   private Web3jWallet wallet = new Web3jWallet();
   private Voting myContract;

    @FXML
    void cast_vote(ActionEvent event) throws Exception {
RadioButton btn = (RadioButton) candidates.getSelectedToggle();
       byte[] name = wallet.asciiToHex(btn.getText());
        myContract.votesForCandidate(name).send();
        switch (btn.getText()){
            case "Asad":
                vote_c1.setText(""+myContract.totalVotesFor(name).send());
                break;
            case "Naveed":
                vote_c2.setText(""+myContract.totalVotesFor(name).send());
                break;
            case "Najeeb":
                vote_c3.setText(""+myContract.totalVotesFor(name).send());
                break;
            case "Iqbal":
                vote_c4.setText(""+myContract.totalVotesFor(name).send());
                break;
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myContract = wallet.getDeployedContract();
        address.setText(address.getText()+myContract.getContractAddress());

        try {
            vote_c1.setText(""+myContract.totalVotesFor(wallet.asciiToHex("Asad")).send());
            vote_c2.setText(""+myContract.totalVotesFor(wallet.asciiToHex("Naveed")).send());
            vote_c3.setText(""+myContract.totalVotesFor(wallet.asciiToHex("Najeeb")).send());
            vote_c4.setText(""+myContract.totalVotesFor(wallet.asciiToHex("Iqbal")).send());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
