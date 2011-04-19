function onError() {
    jQuery.gritter.add({title:'Internal error',text:'The was a problem fulfilling your request, could you please reload the page and try again.',image:'/bluefire/resources/images/growl/error.png', sticky:true});
}

function handleComplete(xhr, status, args) {
    if (status == 'error') {
        if (xhr.status == 500) {
            jQuery.gritter.add({title:'Internal error',text:'The was a problem fulfilling your request, could you please reload the page and try again.',image:'/bluefire/resources/images/growl/error.png', sticky:true});
        }
        return false;
    }
    if (args.validationFailed) {
        return false;
    }
    if (args.result != null) {
        if (args.result.failed) {
            return false;
        }
    }
    return true;
}

function onDeleteComplete(xhr, status, args, dialog) {
    if (handleComplete(xhr, status, args)) {
        dialog.hide();
    }
    return true;
}

function onSaveComplete(xhr, status, args, dialog) {
    if (handleComplete(xhr, status, args)) {
        dialog.hide();
    }
    return true;
}

function onAddComplete(xhr, status, args, dialog) {
    if (handleComplete(xhr, status, args)) {
        dialog.hide();
    }
    return true;
}

function onNewComplete(xhr, status, args, dialog) {
    if (handleComplete(xhr, status, args)) {
        dialog.show();
    }
    return true;
}

function onSearchComplete(xhr, status, args, dialog) {
    if (handleComplete(xhr, status, args)) {
        //No dialog to hide
    }
    return true;
}





function handleRegister(window, event) {
    var s = event['ajaxResult'];
    if ('registered' == s) {
        O$(window).hide();
    }
}

function handleLogin(window, event) {                  
    var s = event['ajaxResult'];
    alert(s);
    if ('loggedIn' == s || true == s || 'true' == s) {
        O$(window).hide();
    } else if (s != null) {
    	alert(s);
    }
}
function handleRegisterRequest(window, xhr, status, args) {
    alert(args.validationFailed);
    alert(args.loggedIn);
    if(args.validationFailed || !args.loggedIn) {
        alert('failed');
        jQuery('#'+window).effect("shake", { times:5 }, 100);
    } else {
        od3.hide();
        jQuery('#l6').fadeOut();
    }
}
