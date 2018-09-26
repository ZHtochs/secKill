//存放主要交互逻辑js代码 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {},
    //详情页秒杀逻辑
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone))
            return true;
        else
            return false;
    },
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录，计时交互
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTIme = params['startTime'];
            var endTIme = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // 未登录，绑定手机号
                //绑定phone，控制输出
                var killPhoneModal = $("#killPhoneModal");
                //显示弹出层
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone='+inputPhone);//TODO
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie("killPhone", inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面，在之前要写入cookie
                        window.location.reload();
                    }else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);

                    }
                })

            }
        }
    }
}
