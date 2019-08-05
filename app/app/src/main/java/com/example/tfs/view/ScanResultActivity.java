package com.example.tfs.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Farm;
import com.example.tfs.dto.Feedings;
import com.example.tfs.dto.Foods;
import com.example.tfs.dto.Provider;
import com.example.tfs.dto.Transaction;
import com.example.tfs.dto.Vaccinations;
import com.example.tfs.service.FoodService;
import com.example.tfs.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanResultActivity extends AppCompatActivity {

    private TextView farm;
    private TextView provider;
    private TextView txtProviderAddress;
    private TextView txtFarmerAddress;
    private TextView tv;
    private Transaction transaction;
    private int transactionId;
    private EditText txtReason;
    private String reason;
    private TextView txtFoodId;
    private TextView txtCateId;
    private TextView txtBreed;
    private TextView txtCreateDate;
    private Dialog myDialog;
    private Button btnConfirm;
    private Button btnReject;
    private int status;
    private int userID;
    private Provider providerDto;
    private Farm farmDto;
    private TextView txtVaccination;
    private TextView txtFeedings;
    private TextView txtTreatment;
    private Process process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        innitView();
        myDialog = new Dialog(this);


        Intent it = this.getIntent();
        transactionId = Integer.parseInt(it.getStringExtra("transactionId").trim());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = sharedPreferences.getInt("userID", 0);
        loadInformation();
    }

    private void innitView(){
        farm = findViewById(R.id.txtFarmer);
        txtFarmerAddress = findViewById(R.id.txtFarmerAddress);
        provider = findViewById(R.id.txtProvider);
        txtProviderAddress = findViewById(R.id.txtProviderAddress);
        tv = findViewById(R.id.txtResult);
        txtBreed = findViewById(R.id.txtBreed);
        txtCateId = findViewById(R.id.txtCateId);
        txtCreateDate = findViewById(R.id.txtCreateDate);
        txtFoodId = findViewById(R.id.txtFoodId);
        txtFeedings = findViewById(R.id.txtFeedings);
        txtTreatment = findViewById(R.id.txtTreatment);
        txtVaccination = findViewById(R.id.txtVaccination);
    }
    public void onClickRejectButton(View v) {
        status = 4;
        showConfirmDialog();
    }

    public void onClickConfirmButton(View v) {
        status = 2;
        showConfirmDialog();
    }

    public void loadInformation() {

        tv = findViewById(R.id.txtResult);

        TransactionService.getInstance().getTransaction(ScanResultActivity.this, transactionId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    String providerName = jsonNode.get("data").get("Provider").get("Name").asText();
                    String provderAddress = jsonNode.get("data").get("Provider").get("Address").asText();
                    String farmerName = jsonNode.get("data").get("Farm").get("Name").asText();
                    String farmerAddress = jsonNode.get("data").get("Farm").get("Address").asText();
                    int foodId = jsonNode.get("data").get("Food").get("FoodId").asInt();
                    int categoryId = jsonNode.get("data").get("Food").get("CategoryId").asInt();
                    String createDate = jsonNode.get("data").get("Food").get("CreatedDate").asText();
                    String breed = jsonNode.get("data").get("Food").get("Breed").asText();
                    //set food dto
                    Foods dto = new Foods();
                    dto.setId(foodId);
                    dto.setCategoryId(categoryId);
                    dto.setCreateDate(createDate);
                    dto.setBreed(breed);
                    //set transaction dto
                    transaction = new Transaction();
                    transaction.setFarmerName(farmerName);
                    transaction.setFarmerAddress(farmerAddress);
                    transaction.setProviderAddress(provderAddress);
                    transaction.setProviderName(providerName);
                    transaction.setFood(dto);

                    farmDto = new Farm();
                    farmDto.setName(farmerName);
                    farmDto.setAddress(farmerAddress);
                    providerDto = new Provider();
                    providerDto.setName(providerName);
                    providerDto.setAddress(provderAddress);

                    txtFoodId.setText("" + transaction.getFood().getId());
                    txtCreateDate.setText("" + transaction.getFood().getCreateDate());
                    txtBreed.setText("" + transaction.getFood().getBreed());
                    txtCateId.setText("" + transaction.getFood().getCategoryId());
                    farm.setText("" + transaction.getFarmerName());
                    txtFarmerAddress.setText("" + transaction.getFarmerAddress());
                    provider.setText("" + transaction.getProviderName());
                    txtProviderAddress.setText("" + transaction.getProviderAddress());

                    loadFoodData(foodId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object ex) {
                tv.setText("load information that bai");
            }
        });
    }

    public void loadFoodData(int foodId) {
        FoodService.getInstance().getFoods(ScanResultActivity.this, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);

                    String farmm = json.substring(json.indexOf("Farm") + 6, json.indexOf("Provider") - 2);

                    txtCateId.setText(jsonNode.get("Category").asText());

//                    Toast.makeText(ScanResultActivity.this, farmm, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = new JSONObject(farmm);
                    //get Feedings
                    List<String> feed = new ArrayList<>();
                    JSONArray feedings = jsonObject.getJSONArray("Feedings");
                    for(int i = 0; i < feedings.length(); i++) {
                        feed.add(feedings.getString(i));
                    }
                    farmDto.setFeedings(feed);
//                    //get Vaccinations
                    List<Vaccinations> listVaccin = new ArrayList<>();
                    JSONArray vaccinations = jsonObject.getJSONArray("Vaccinations");
                    for(int i = 0; i < vaccinations.length(); i++) {
                        Vaccinations dto = new Vaccinations();
                        JSONObject object = (JSONObject) vaccinations.get(i);
                        dto.setVaccinationDate(object.getString("VaccinationDate"));
                        dto.setVaccinationType(object.getString("VaccinationType"));
                        listVaccin.add(dto);
                    }
                    farmDto.setVaccinations(listVaccin);


//                    String treatmentDate = jsonNode.get("Treatment").get("TreatmentDate").asText();
//                    JSONArray treatmentProcess = jsonObject.getJSONArray("TreatmentProcess");
//                    for(int i = 0; i < treatmentProcess.length(); i++) {
//                        providerDto.getTreatment().getTreatmentProcess().add(treatmentProcess.getString(i));
//                    }
//                    providerDto.getTreatment().setTreatmentDate(treatmentDate);
//                    providerDto.setAddress(providerAddress);
//                    providerDto.setName(providerName);



                    String vaccin = "";
                    for(int i = 0; i < farmDto.getVaccinations().size(); i ++) {
                        vaccin += "+ " + farmDto.getVaccinations().get(i).getVaccinationDate().substring(0,10) + "\n" + farmDto.getVaccinations().get(i).getVaccinationType() + "\n";
                    }
                    txtVaccination.setText(vaccin);
//                    Toast.makeText(ScanResultActivity.this, vaccinations.length(), Toast.LENGTH_SHORT).show();
                    String feeding = "";
                    for(int i = 0; i < farmDto.getFeedings().size(); i++) {
                        feeding += "+ " + farmDto.getFeedings().get(i) + "\n";
                    }

                    txtFeedings.setText(feeding);
//
//                    String treatmentProcesss = "";
//                    for(int i = 0; i < providerDto.getTreatment().getTreatmentProcess().size(); i++) {
//                        treatmentProcesss += providerDto.getTreatment().getTreatmentProcess().get(i);
//                    }
//                    txtTreatment.setText(treatmentProcesss);
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Object ex) {

            }
        });
    }

    public void initInformation() {
        txtFoodId.setText("" + transaction.getFood().getId());
        txtCreateDate.setText("" + transaction.getFood().getCreateDate());
        txtBreed.setText("" + transaction.getFood().getBreed());
        txtCateId.setText("" + transaction.getFood().getCategoryId());
        farm.setText("" + transaction.getFarmerName());
        txtFarmerAddress.setText("" + transaction.getFarmerAddress());
        provider.setText("" + transaction.getProviderName());
        txtProviderAddress.setText("" + transaction.getProviderAddress());
        String vaccin = "";
        for(int i = 0; i < farmDto.getVaccinations().size(); i ++) {
            vaccin += farmDto.getVaccinations().get(i).getVaccinationDate() + "  " + farmDto.getVaccinations().get(i).getVaccinationType();
        }
        txtVaccination.setText(vaccin);

        String feeding = "";
        for(int i = 0; i < farmDto.getFeedings().size(); i++) {
            feeding += farmDto.getFeedings().get(i);
        }
        txtFeedings.setText(feeding);

        String treatmentProcess = "";
        for(int i = 0; i < providerDto.getTreatment().getTreatmentProcess().size(); i++) {
            treatmentProcess += providerDto.getTreatment().getTreatmentProcess().get(i);
        }
        txtTreatment.setText(treatmentProcess);
    }


    public void showConfirmDialog() {
        TextView txtClose;
        myDialog.setContentView(R.layout.confirm_reason_dialog);
        txtClose = myDialog.findViewById(R.id.txtClose);
        btnConfirm = myDialog.findViewById(R.id.btnConfirm);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtReason = myDialog.findViewById(R.id.txtReason);
                reason = txtReason.getText().toString().trim();
                if (status == 4 && reason.length() == 0) {
                    TextView txtReasonValidation = myDialog.findViewById(R.id.txtReasonValidation);
                    txtReasonValidation.setText("Vui lòng nhập lý do");
                } else {
                    confirmTransaction();
                }
            }
        });
        myDialog.show();
    }

    public void confirmTransaction() {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        TransactionService.getInstance().updateTransaction(ScanResultActivity.this, transactionId, status, reason, userID, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(Object ex) {

            }
        });

    }
}
