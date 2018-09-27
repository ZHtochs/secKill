//存放主要交互逻辑js代码 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    //处理秒杀逻辑
    handleSeckill: function (seckillId, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>')
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀，显示按钮
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl:' + killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //绑定按钮显示，执行秒杀请求
                        //1.先禁用按钮
                        $(this).addClass('disabled');
                        //2.发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            console.log(result);//todo
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀结果
                                console.log('killUrl:');//todo
                                node.html('<span class="label label-success">' + stateInfo + '</span>')
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开始
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    console.log('重新计算计时逻辑');//todo
                    seckill.countdown(seckillId, now, start, end);

                }
            } else {
                console.log('result:' + result);
            }
        });
    },
    //详情页秒杀逻辑
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone))
            return true;
        else
            return false;
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box')
        //时间判断
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html("秒杀结束");
        }
        else if (nowTime < startTime) {
            seckillBox.html("秒杀未开始");
            //计时
            var killTime = new Date(startTime + 1000);

            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D 天 %H 时 %M 分 %S 秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                console.log('______fininsh.countdown1');
                seckill.handleSeckill(seckillId, seckillBox);
            });
        }else {
            //秒杀开始
            console.log('______fininsh.countdown2');
            seckill.handleSeckill(seckillId, seckillBox);//处理秒杀逻辑
        }

    },
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录，计时交互
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

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
                });

            }
            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            // $.get是jquary的ajax get请求,  result 是请求的返回结果是json格式的
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //事件判断,计时交互
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);//TODO
                }

            });
        }
    }
}
