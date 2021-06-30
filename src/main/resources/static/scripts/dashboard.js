const DELETE_ACC_URL = "/delete_account";
const LOGOUT_URL = "/logout";


const initialize = function(){
    $("#delete-acc-modal").modal("hide");
    $("#logout-form").hide();
    $("#delete-form").hide();
};

$(document).ready(function(){
   
    initialize();

    $("#delete-acc-btn").click(()=>{
        $("#delete-acc-modal").modal("show");
    });

    $("#delete-acc-confirm-btn").click(()=>{
        $("#delete-submit-btn").click();
    });

    $("#logout-btn").click(()=>{
        $("#logout-submit-btn").click();
    });

});