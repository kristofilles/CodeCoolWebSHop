$(document).ready(function(){
    $("#shipping").change(function(){
    var selectedAddress = $("#shipping option:selected").data();
    if ($.isEmptyObject(selectedAddress)){
        $("#form").trigger("reset");
    } else {
        for (var i in selectedAddress) {
            $("#" + i).val(selectedAddress[i]);
        }
    }
    });
});