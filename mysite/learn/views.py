from django.shortcuts import render
from django.contrib import auth
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.contrib.auth.decorators import login_required
from django.db.models import Avg, Max, Min, Count
from .forms import UserForm
from .models import Athlete,Team,person_first
# Create your views here.
# coding:utf-8
from django.http import HttpResponse
from django.forms.models import model_to_dict
import os


#def login(request):
#    return render(request, 'logIn.html')

def userPage(request):
    if request.method == 'POST':
        if 'notload' in request.POST:
            max_athnum = (Athlete.objects.aggregate(Max('athletes_number')).get('athletes_number__max'))
            #
            team_name = request.POST["team_name"]
            #
            leader_name = request.POST["leader_name"]
            leader_id = request.POST["leader_id"]
            leader_telephone = request.POST["leader_telephone"]
            #
            doctor_name = request.POST["doctor_name"]
            doctor_id = request.POST["doctor_id"]
            doctor_telephone = request.POST["doctor_telephone"]
            #
            coach_name = request.POST["coach_name"]
            coach_id = request.POST["coach_id"]
            coach_telephone = request.POST["coach_telephone"]
            #
            referee_name = request.POST["referee_name"]
            referee_id = request.POST["referee_id"]
            referee_telephone = request.POST["referee_telephone"]
            referee_password = request.POST["referee_password"]
            #创建team对象
            if(leader_name!=''):
                Team.objects.get_or_create(team_name=team_name,leader_name=leader_name,leader_id=leader_id,leader_telephone=leader_telephone,doctor_name=doctor_name,doctor_id=doctor_id,doctor_telephone=doctor_telephone,
                coach_name=coach_name,coach_id=coach_id,coach_telephone=coach_telephone,referee_name=referee_name,referee_id=referee_id,referee_telephone=referee_telephone,coach_sex=1,referee_password=referee_password)
            #1
            athletes_name1 = request.POST["athletes_name1"]
            if(athletes_name1!=''):
                #如果不为空的话
                athletes_id1 = request.POST["athletes_id1"]
                #
                athletes_age1 = int(request.POST["athletes_age1"])
                if(athletes_age1<=8):
                    team_type1=1
                elif(athletes_age1<=10):
                    team_type1 = 2
                else:
                    team_type1 = 3
                #
                if (max_athnum == None):
                    max_athnum=0
                max_athnum = max_athnum + 1
                athletes_number1 = max_athnum
                #判断性别
                athletes_sex1 = int(request.POST["athletes_sex1"])
                if(athletes_sex1):#如果是男生
                    Boy1DG = request.POST["Boy1DG"]
                    if(Boy1DG=='1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1,athletes_id=athletes_id1,
                                               athletes_age=athletes_age1,team_type=team_type1,event_type='DG',
                                               athletes_sex=athletes_sex1,athletes_number=athletes_number1)
                    Boy1SG = request.POST["Boy1SG"]
                    if(Boy1SG=='1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='SG',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Boy1DH = request.POST["Boy1DH"]
                    if (Boy1DH == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='DH',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Boy1TM = request.POST["Boy1TM"]
                    if (Boy1TM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='TM',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Boy1AM = request.POST["Boy1AM"]
                    if (Boy1AM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='AM',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Boy1BC = request.POST["Boy1BC"]
                    if (Boy1BC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='BC',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Boy1ZYTC = request.POST["Boy1ZYTC"]
                    if (Boy1ZYTC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='ZYTC',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                else:
                    Girl1TM = request.POST["Girl1TM"]
                    if (Girl1TM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='TM',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Girl1GDG = request.POST["Girl1GDG"]
                    if (Girl1GDG == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='GDG',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Girl1PHM = request.POST["Girl1PHM"]
                    if (Girl1PHM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='PHM',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Girl1ZYTC = request.POST["Girl1ZYTC"]
                    if (Girl1ZYTC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='ZYTC',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
                    Girl1BC = request.POST["Girl1BC"]
                    if (Girl1BC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name1, athletes_id=athletes_id1,
                                               athletes_age=athletes_age1, team_type=team_type1, event_type='BC',
                                               athletes_sex=athletes_sex1, athletes_number=athletes_number1)
            # 2
            athletes_name2 = request.POST["athletes_name2"]
            if (athletes_name2 != ''):
                # 如果不为空的话
                athletes_id2 = request.POST["athletes_id2"]
                #
                athletes_age2 = int(request.POST["athletes_age2"])
                if (athletes_age2 <= 8):
                    team_type2 = 1
                elif (athletes_age2 <= 10):
                    team_type2 = 2
                else:
                    team_type2 = 3
                #
                if (max_athnum == None):
                    max_athnum = 0
                max_athnum = max_athnum + 1
                athletes_number2 = max_athnum
                # 判断性别
                athletes_sex2 = int(request.POST["athletes_sex2"])
                if (athletes_sex2):  # 如果是男生
                    Boy2DG = request.POST["Boy2DG"]
                    if (Boy2DG == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='DG',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2SG = request.POST["Boy2SG"]
                    if (Boy2SG == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='SG',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2DH = request.POST["Boy2DH"]
                    if (Boy2DH == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='DH',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2TM = request.POST["Boy2TM"]
                    if (Boy2TM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='TM',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2AM = request.POST["Boy2AM"]
                    if (Boy2AM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='AM',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2BC = request.POST["Boy2BC"]
                    if (Boy2BC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='BC',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Boy2ZYTC = request.POST["Boy2ZYTC"]
                    if (Boy2ZYTC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2,
                                                      event_type='ZYTC',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                else:
                    Girl2TM = request.POST["Girl2TM"]
                    if (Girl2TM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='TM',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Girl2GDG = request.POST["Girl2GDG"]
                    if (Girl2GDG == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2,
                                                      event_type='GDG',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Girl2PHM = request.POST["Girl2PHM"]
                    if (Girl2PHM == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2,
                                                      event_type='PHM',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Girl2ZYTC = request.POST["Girl2ZYTC"]
                    if (Girl2ZYTC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2,
                                                      event_type='ZYTC',
                                                      athletes_sex=athletes_sex2, athletes_number=athletes_number2)
                    Girl2BC = request.POST["Girl2BC"]
                    if (Girl2BC == '1'):
                        Athlete.objects.get_or_create(athletes_name=athletes_name2, athletes_id=athletes_id2,
                                                      athletes_age=athletes_age2, team_type=team_type2, event_type='BC',athletes_sex=athletes_sex2, athletes_number=athletes_number2)
            return render(request, 'userPage.html')
        else:
            myFile = request.FILES.get("myfile", None)  # 获取上传的文件，如果没有文件，则默认为None
            password=request.POST['password']
            username=request.POST['username']
            context = {}
            context['password'] = password
            context['username'] = username
            if not myFile:
                return render(request, 'uploadFail.html', context)
            destination = open(os.path.join("/Users/jiaoma/Desktop", myFile.name), 'wb+')  # 打开特定的文件进行二进制的写操作
            for chunk in myFile.chunks():  # 分块写入文件
                destination.write(chunk)
            destination.close()
            return render(request,'uploadSucc.html',context)

def arrangePage(request):
    event_type = request.POST["event_type"]
    xuanshou_1 = request.POST["xuanshou_1"]
    xuanshou_2 = request.POST["xuanshou_2"]
    xuanshou_3 = request.POST["xuanshou_3"]
    xuanshou_4 = request.POST["xuanshou_4"]
    xuanshou_5 = request.POST["xuanshou_5"]
    xuanshou_6 = request.POST["xuanshou_6"]
    xuanshou_7 = request.POST["xuanshou_7"]
    print(event_type)
    if(xuanshou_1 != ''):
        result=Athlete.objects.filter(athletes_name=xuanshou_1,event_type=event_type).values()
        print(xuanshou_1)
        if(result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'],event_type=result[0]['event_type'],athletes_sex=result[0]['athletes_sex'],team_type=result[0]['team_type'],athletes_name=result[0]['athletes_name'],total_score=0)

    if (xuanshou_2 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_2, event_type=event_type).values()
        print(xuanshou_2)
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    if (xuanshou_3 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_3, event_type=event_type).values()
        print(xuanshou_3)
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    if (xuanshou_4 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_4, event_type=event_type).values()
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    if (xuanshou_5 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_5, event_type=event_type).values()
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    if (xuanshou_6 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_6, event_type=event_type).values()
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    if (xuanshou_7 != ''):
        result = Athlete.objects.filter(athletes_name=xuanshou_7, event_type=event_type).values()
        if (result):
            person_first.objects.create(athletes_id=result[0]['athletes_id'], event_type=result[0]['event_type'],
                                        athletes_sex=result[0]['athletes_sex'], team_type=result[0]['team_type'],
                                        athletes_name=result[0]['athletes_name'], total_score=0)
    return render(request,'arrangePage.html')

#登陆
#@csrf_exempt
def login(req):
    context = {}
    if req.method == 'POST':
        form = UserForm(req.POST)
        if form.is_valid():
            #获取表单用户密码
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            print(username,password)
            #获取的表单数据与数据库进行比较
            user = authenticate(username = username,password = password)
            if user:
                context = username
                #比较成功，跳转index
                if username =='jiaoma':
                    auth.login(req, user)
                    req.session['username'] = username
                    return render(req, 'arrangePage.html')
                else:
                    auth.login(req,user)
                    context = {}
                    context['teamname'] = username
                    context['password'] = password
                    req.session['username'] = username
                    return render(req, 'userPage.html',context)
            else:
                #比较失败，还在login
                return render(req, 'logIn.html')
        else:
            print('++++++++++')
    context = {'isLogin': False,'pswd':True}
    return render(req, 'logIn.html')

def getAthletes(req):
    athletes=list(Athlete.objects.all().values())# 查询所有
    str1="Aththeles info are as follows: <br>"
    for athlete in athletes:
        str1+=str(athlete)
        str1+='<br>'
    print(str1)
    return HttpResponse(str1)