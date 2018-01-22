from django.db import models

# Create your models here.
class Team(models.Model):
    team_name=models.CharField(max_length=30,primary_key=True)
    #
    leader_name=models.CharField(max_length=30)
    leader_id = models.CharField(max_length=30)
    leader_telephone = models.CharField(max_length=30)
    #
    doctor_name = models.CharField(max_length=30)
    doctor_id = models.CharField(max_length=30)
    doctor_telephone = models.CharField(max_length=30)
    #
    coach_name=models.CharField(max_length=30)
    coach_id = models.CharField(max_length=30)
    coach_telephone = models.CharField(max_length=30)
    coach_sex=models.IntegerField()
    #
    referee_name=models.CharField(max_length=30)
    referee_id=models.CharField(max_length=30)
    referee_telephone=models.CharField(max_length=30)
    referee_password = models.CharField(max_length=30,default="")

class Athlete(models.Model):
    athletes_name=models.CharField(max_length=30)
    athletes_id=models.CharField(max_length=30)
    athletes_age=models.IntegerField()
    team_type = models.IntegerField()
    event_type=models.CharField(max_length=30)
    athletes_sex = models.IntegerField()
    athletes_number=models.IntegerField()
    class Meta:
        db_table = 'learn_Athlete'
        unique_together = ("athletes_number", "event_type")  # 这是重点

class person_first(models.Model):
    athletes_id=models.CharField(max_length=30)
    event_type=models.CharField(max_length=30)
    total_score = models.IntegerField()
    athletes_sex = models.IntegerField()
    team_type = models.IntegerField()
    athletes_name = models.CharField(max_length=30,default='')

class person_final(models.Model):
    athletes_id=models.CharField(max_length=30)
    event_type=models.CharField(max_length=30)
    total_score = models.IntegerField()
    athletes_sex = models.IntegerField()
    team_type = models.IntegerField()
    athletes_name = models.CharField(max_length=30,default='')


