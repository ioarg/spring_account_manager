const CREATE_ACC_URL = "/create_account";
const create_acc_success = function(data, status, jqXHR){
    console.log(data);
}


$(document).ready(function(){
    $("#create-modal").modal("hide");


    /*************************
     * Create an account
    *************************/

    $("#create-acc-modal-btn").click(()=>{
        $("#create-modal").modal("show");
    });

    $("#create-acc-btn").click(()=>{
        let formData = $("#create-acc-form").serialize();
        
        $.post(CREATE_ACC_URL, formData, create_acc_success, "text")
            .fail(()=>{ console.log("Error in creating account.")});
    });

});