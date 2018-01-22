from django import forms

class UserForm(forms.Form):
    username = forms.CharField(max_length=30)
    password = forms.CharField(max_length=50)


class TeamForm(forms.Form):
    team_name = forms.CharField(max_length=30)
    #
    leader_name = forms.CharField(max_length=30)
    leader_id = forms.CharField(max_length=30)
    leader_telephone = forms.CharField(max_length=30)
    #
    doctor_name = forms.CharField(max_length=30)
    doctor_id = forms.CharField(max_length=30)
    doctor_telephone = forms.CharField(max_length=30)
    #
    coach_name = forms.CharField(max_length=30)
    coach_id = forms.CharField(max_length=30)
    coach_telephone = forms.CharField(max_length=30)
    coach_sex = forms.IntegerField()#为空
    #
    referee_name = forms.CharField(max_length=30)
    referee_id = forms.CharField(max_length=30)
    referee_telephone = forms.CharField(max_length=30)
    referee_password = forms.CharField(max_length=30)
    # 1
    athletes_name1 = forms.CharField(max_length=30)
    athletes_id1 = forms.CharField(max_length=30)
    athletes_age1 = forms.IntegerField()
    team_type1 = forms.IntegerField()
    event_type1 = forms.CharField(max_length=30)
    athletes_sex1 = forms.IntegerField()
    athletes_number1 = forms.CharField(max_length=30)
    # 2
    athletes_name2 = forms.CharField(max_length=30)
    athletes_id2 = forms.CharField(max_length=30)
    athletes_age2 = forms.IntegerField()
    team_type2 = forms.IntegerField()
    event_type2 = forms.CharField(max_length=30)
    athletes_sex2 = forms.IntegerField()
    athletes_number2 = forms.CharField(max_length=30)
    # 3
    athletes_name3 = forms.CharField(max_length=30)
    athletes_id3 = forms.CharField(max_length=30)
    athletes_age3 = forms.IntegerField()
    team_type3 = forms.IntegerField()
    event_type3 = forms.CharField(max_length=30)
    athletes_sex3 = forms.IntegerField()
    athletes_number3 = forms.CharField(max_length=30)
    # 4
    athletes_name4 = forms.CharField(max_length=30)
    athletes_id4 = forms.CharField(max_length=30)
    athletes_age4 = forms.IntegerField()
    team_type4 = forms.IntegerField()
    event_type4 = forms.CharField(max_length=30)
    athletes_sex4 = forms.IntegerField()
    athletes_number4 = forms.CharField(max_length=30)
    # 5
    athletes_name5 = forms.CharField(max_length=30)
    athletes_id5 = forms.CharField(max_length=30)
    athletes_age5 = forms.IntegerField()
    team_type5 = forms.IntegerField()
    event_type5 = forms.CharField(max_length=30)
    athletes_sex5 = forms.IntegerField()
    athletes_number5 = forms.CharField(max_length=30)
    # 6
    athletes_name6 = forms.CharField(max_length=30)
    athletes_id6 = forms.CharField(max_length=30)
    athletes_age6 = forms.IntegerField()
    team_type6 = forms.IntegerField()
    event_type6 = forms.CharField(max_length=30)
    athletes_sex6 = forms.IntegerField()
    athletes_number6 = forms.CharField(max_length=30)
    # 7
    athletes_name7 = forms.CharField(max_length=30)
    athletes_id7 = forms.CharField(max_length=30)
    athletes_age7 = forms.IntegerField()
    team_type7 = forms.IntegerField()
    event_type7 = forms.CharField(max_length=30)
    athletes_sex7 = forms.IntegerField()
    athletes_number7 = forms.CharField(max_length=30)
    # 8
    athletes_name8 = forms.CharField(max_length=30)
    athletes_id8 = forms.CharField(max_length=30)
    athletes_age8 = forms.IntegerField()
    team_type8 = forms.IntegerField()
    event_type8 = forms.CharField(max_length=30)
    athletes_sex8 = forms.IntegerField()
    athletes_number8 = forms.CharField(max_length=30)