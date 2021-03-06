package com.example.ve183011m.pki_ve183011m.presentation.registration;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;

import com.example.ve183011m.pki_ve183011m.data.UserManager;
import com.example.ve183011m.pki_ve183011m.model.Handyman;
import com.example.ve183011m.pki_ve183011m.model.Job;
import com.example.ve183011m.pki_ve183011m.model.Request;
import com.example.ve183011m.pki_ve183011m.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class RegistrationVM extends BaseObservable implements Serializable {

    public interface RegistrationFirstStepCallback {
        void onEmptyUsername();

        void onEmptyPassword();

        void onEmptySecondPassword();

        void onDifferentPasswords();

        void onEmptyFullName();
    }

    public interface RegistrationSecondStepCallback {
        void onEmptyPhone();

        void onEmptyAddress();
    }

    private final UserManager userManager;
    private final RegistrationHandler registrationHandler;

    private ObservableField<String> username;
    private ObservableField<String> password;
    private ObservableField<String> passwordSecond;
    private ObservableField<String> fullName;
    private ObservableField<String> telephone;
    private ObservableField<String> address;
    private ObservableBoolean isBuyer;
    private ObservableInt experience;
    private ObservableBoolean plumbing;
    private ObservableFloat plumbingPrice;
    private ObservableBoolean electrical;
    private ObservableFloat electricalPrice;
    private ObservableBoolean furniture;
    private ObservableFloat furniturePrice;
    private ObservableBoolean ac;
    private ObservableFloat acPrice;
    private ObservableBoolean paintJob;
    private ObservableFloat paintJobPrice;

    private int step = 0;

    RegistrationVM(RegistrationHandler handler) {
        this.userManager = UserManager.getInstance();
        registrationHandler = handler;
        username = new ObservableField<>();
        password = new ObservableField<>();
        passwordSecond = new ObservableField<>();
        fullName = new ObservableField<>();
        telephone = new ObservableField<>();
        address = new ObservableField<>();
        isBuyer = new ObservableBoolean(true);
        experience = new ObservableInt(1);
        plumbing = new ObservableBoolean();
        electrical = new ObservableBoolean();
        furniture = new ObservableBoolean();
        ac = new ObservableBoolean();
        paintJob = new ObservableBoolean();
        plumbingPrice = new ObservableFloat();
        electricalPrice = new ObservableFloat();
        furniturePrice = new ObservableFloat();
        acPrice = new ObservableFloat();
        paintJobPrice = new ObservableFloat();
    }

    public void next() {
        switch (step) {
            case 0: {
                step++;
                registrationHandler.onNext();
                break;
            }
            case 1: {
                step++;
                if (isBuyer.get()) {
                    User user = new User(username.get(), password.get(), fullName.get(), address.get()
                            , telephone.get());
                    userManager.addUser(user);
                    registrationHandler.onRegister(user);
                } else {
                    registrationHandler.onNext();
                }
                break;
            }
            case 2: {
                ArrayList<Job> skills = new ArrayList<>();
                if (plumbing.get()) {
                    skills.add(new Job("Plumbing", plumbingPrice.get()));
                }
                if (electrical.get()) {
                    skills.add(new Job("Electrical installations", electricalPrice.get()));
                }
                if (furniture.get()) {
                    skills.add(new Job("Furniture assembly", furniturePrice.get()));
                }
                if (ac.get()) {
                    skills.add(new Job("Air conditioning", acPrice.get()));
                }
                if (paintJob.get()) {
                    skills.add(new Job("Interior painting", paintJobPrice.get()));
                }
                User user = new Handyman(username.get(), password.get(), fullName.get(), address.get(),
                        telephone.get(), experience.get(), skills, new ArrayList<Float>(), new ArrayList<Handyman.Review>());
                userManager.addUser(user);
                registrationHandler.onRegister(user);
                break;
            }
        }
    }

    public void back() {
        if (step == 1 || step == 2) {
            step--;
            registrationHandler.onBack();
        }
    }

    public boolean validateFirstStepInput(RegistrationFirstStepCallback callback) {
        boolean isValid = true;
        if (!validateUsername(callback)) {
            isValid = false;
        }
        if (!validateSecondPassword(callback)) {
            isValid = false;
        }
        if (!validateFullName(callback)) {
            isValid = false;
        }
        return isValid;
    }

    public boolean validateSecondStepInput(RegistrationSecondStepCallback callback) {
        boolean isValid = true;
        if (!validatePhone(callback)) {
            isValid = false;
        }
        if (!validateAddress(callback)) {
            isValid = false;
        }
        return isValid;
    }

    public boolean validateUsername(RegistrationFirstStepCallback callback) {
        String user = username.get();
        if (user == null || user.isEmpty()) {
            callback.onEmptyUsername();
            return false;
        }
        return true;
    }

    public boolean validatePassword(RegistrationFirstStepCallback callback) {
        String pass = password.get();
        if (pass == null || pass.isEmpty()) {
            callback.onEmptyPassword();
            return false;
        }
        return true;
    }

    public boolean validateSecondPassword(RegistrationFirstStepCallback callback) {
        String secondPass = passwordSecond.get();
        if (secondPass == null || secondPass.isEmpty()) {
            callback.onEmptySecondPassword();
            validatePassword(callback);
            return false;
        }

        String firstPass = password.get();
        if (validatePassword(callback) && !firstPass.equals(secondPass)) {
            callback.onDifferentPasswords();
            return false;
        }
        return true;
    }

    public boolean validateFullName(RegistrationFirstStepCallback callback) {
        String name = fullName.get();
        if (name == null || name.isEmpty()) {
            callback.onEmptyFullName();
            return false;
        }
        return true;
    }

    public boolean validatePhone(RegistrationSecondStepCallback callback) {
        String phone = telephone.get();
        if (phone == null || phone.isEmpty()) {
            callback.onEmptyPhone();
            return false;
        }
        return true;
    }

    public boolean validateAddress(RegistrationSecondStepCallback callback) {
        String add = address.get();
        if (add == null || add.isEmpty()) {
            callback.onEmptyAddress();
            return false;
        }
        return true;
    }

    public ObservableField<String> getUsername() {
        return username;
    }

    public void setUsername(ObservableField<String> username) {
        this.username = username;
    }

    public ObservableField<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableField<String> password) {
        this.password = password;
    }

    public ObservableField<String> getPasswordSecond() {
        return passwordSecond;
    }

    public void setPasswordSecond(ObservableField<String> passwordSecond) {
        this.passwordSecond = passwordSecond;
    }

    public ObservableField<String> getFullName() {
        return fullName;
    }

    public void setFullName(ObservableField<String> fullName) {
        this.fullName = fullName;
    }

    public ObservableField<String> getTelephone() {
        return telephone;
    }

    public void setTelephone(ObservableField<String> telephone) {
        this.telephone = telephone;
    }

    public ObservableField<String> getAddress() {
        return address;
    }

    public void setAddress(ObservableField<String> address) {
        this.address = address;
    }

    public ObservableBoolean getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(ObservableBoolean isBuyer) {
        this.isBuyer = isBuyer;
    }

    public ObservableInt getExperience() {
        return experience;
    }

    public void setExperience(ObservableInt experience) {
        this.experience = experience;
    }

    public ObservableBoolean getPlumbing() {
        return plumbing;
    }

    public void setPlumbing(ObservableBoolean plumbing) {
        this.plumbing = plumbing;
    }

    public ObservableBoolean getElectrical() {
        return electrical;
    }

    public void setElectrical(ObservableBoolean electrical) {
        this.electrical = electrical;
    }

    public ObservableBoolean getFurniture() {
        return furniture;
    }

    public void setFurniture(ObservableBoolean furniture) {
        this.furniture = furniture;
    }

    public ObservableBoolean getAc() {
        return ac;
    }

    public void setAc(ObservableBoolean ac) {
        this.ac = ac;
    }

    public ObservableBoolean getPaintJob() {
        return paintJob;
    }

    public void setPaintJob(ObservableBoolean paintJob) {
        this.paintJob = paintJob;
    }

    public int getStep() {
        return step;
    }

    public String getExperienceText() {
        return String.valueOf(experience.get());
    }

    public ObservableFloat getPlumbingPrice() {
        return plumbingPrice;
    }

    public void setPlumbingPrice(ObservableFloat plumbingPrice) {
        this.plumbingPrice = plumbingPrice;
    }

    public ObservableFloat getElectricalPrice() {
        return electricalPrice;
    }

    public void setElectricalPrice(ObservableFloat electricalPrice) {
        this.electricalPrice = electricalPrice;
    }

    public ObservableFloat getFurniturePrice() {
        return furniturePrice;
    }

    public void setFurniturePrice(ObservableFloat furniturePrice) {
        this.furniturePrice = furniturePrice;
    }

    public ObservableFloat getAcPrice() {
        return acPrice;
    }

    public void setAcPrice(ObservableFloat acPrice) {
        this.acPrice = acPrice;
    }

    public ObservableFloat getPaintJobPrice() {
        return paintJobPrice;
    }

    public void setPaintJobPrice(ObservableFloat paintJobPrice) {
        this.paintJobPrice = paintJobPrice;
    }
}
