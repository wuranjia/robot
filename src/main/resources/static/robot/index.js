$(function () {
    //
    $('#ck').empty().append("<span style='color：#F00'>加载好友列表加载中.....</span>");
    //setTimeout(loadCheckBox(), 1000);
    check();
});

function change() {
    var img = document.getElementById("imageRobotConfig");
    img.src = "/file/step2.png";
    var fileName = Math.random();
    $.get("/wechat/start?fileName=" + fileName, function (result) {
        if ("success" == result) {
            img.src = "/file/" + fileName + ".jpg";
            setTimeout(load(), 5000);
        } else {
            img.src = "/file/head.png";
            loadCheckBox();
        }
    });
}

function check() {
    var img = document.getElementById("imageRobotConfig");
    img.src = "/file/head.png";
    $.get("/wechat/check?" + Math.random(), function (result) {
        if ("success" == result) {
            img.src = "/file/head.png";
            loadCheckBox();
        } else {
            img.src = "/file/step2.png";
        }
    });
}

function load() {
    $.get("/wechat/status?" + Math.random(), function (str) {
        var result = JSON.parse(str);

        if (result.login == 'online' && result.load == 'ready') {
            changeImgLogin();
            loadCheckBox();
        } else if (result.login == 'online' && result.load == 'unready') {
            changeImgLogin();
        } else {
            setTimeout(load(), 5000);
        }
    });
}

/**
 * 修改二维码图片为已登录的用户二维码
 */
function changeImgLogin() {
    var img = document.getElementById("imageRobotConfig");
    $.get("/wechat/head?" + +Math.random(), function (result) {
        if ("success" == result) {
            img.src = "/file/head.png";
        } else {
            setTimeout(changeImgLogin(), 5000);
        }
    });
}

function loadCheckBox() {
    $.get("/wechat/contactList?" + Math.random(), function (str) {
        var result = JSON.parse(str);
        //alert(result.code);
        //alert(result.code != "success");
        if (result.code != "success") {
            $('#ck').empty().append("<span style='color：#F00'>加载好友列表加载中.....</span>");
            setInterval(loadCheckBox(), 5000);
        } else {
            var html = "";
            var array = result.data;
            for (var i = 0; i < array.length; i++) {
                var entity = array[i];
                if (entity.checked == 'checked') {

                    html += "<label class='checkbox'>"
                        + "<input type='checkbox' value='"
                        + entity.nickName
                        + "' checked>"
                        + entity.remarkName
                        + "</input></label>";
                } else {
                    html += "<label class='checkbox'>"
                        + "<input type='checkbox' value='"
                        + entity.nickName
                        + "'>"
                        + entity.remarkName
                        + "</input></label>";
                }
            }

            $('#ck').empty().append(html);
        }
    });
}

function addAll() {
    $.get("/wechat/addAll?" + Math.random(), function (result) {
        alert("添加成功!");
        loadCheckBox();
    });
}

function delAll() {
    $.get("/wechat/delAll?" + Math.random(), function (result) {
        alert("移除成功!");
        loadCheckBox();
    });
}

function save() {
    var params = '';
    $.each($('input:checkbox:checked'), function () {
        //window.alert("你选了："+
        //    $('input[type=checkbox]:checked').length+"个，其中有："+$(this).val());
        params += $(this).val() + ",";
    });
    params = params.substr(0, params.length - 1);
    if (params.length == 0) {
        alert("参数为空");
        return;
    }
    //alert(params);
    var obj = {
        "nickNames": params
    };

    $.ajax({
        type: 'POST',
        url: "/wechat/update",
        data: JSON.stringify(obj),
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            alert(result);
            loadCheckBox();
        }
    });
}