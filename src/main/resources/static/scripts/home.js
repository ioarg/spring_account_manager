const CREATE_ACC_URL = "/create_account";

const create_acc_success = function(){
    $("#create-acc-form").hide();
    $("#create-modal-footer").hide();
    $("#create-acc-form-failure").hide();
    $("#create-acc-form-success").show();
}

const create_acc_failure = function(data){
    console.log(data);
    $("#create-acc-form").hide();
    $("#create-modal-footer").hide();
    $("#create-acc-form-success").hide();
    $("#create-acc-form-failure").text(data);
    $("#create-acc-form-failure").show();
}

const initializeModal = function(){
    $("#create-modal").modal("hide");
    $("#create-acc-form-success").hide();
    $("#create-acc-form-failure").hide();
};

const resetModalBody = function(){
    $("#create-acc-form-success").hide();
    $("#create-acc-form-failure").hide();
    $("#create-acc-form").show();
    $("#create-modal-footer").show();
}

$(document).ready(function(){
   
    initializeModal();

    /*************************
     * Create an account
    *************************/
   
    $("#create-acc-modal-btn").click(()=>{
        resetModalBody();
        $("#create-modal").modal("show");
    });

    $("#create-acc-btn").click(()=>{
        let formData = $("#create-acc-form").serialize();
        console.log(formData);
        $.post(CREATE_ACC_URL, formData, create_acc_success, "text")
            .fail((jqXHR)=>create_acc_failure(jqXHR.responseText));
    });

});