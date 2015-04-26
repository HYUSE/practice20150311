# -*- coding :utf-8 -*-
from django.db import models

class RegionDo(models.Model):
    name = models.CharField(max_length=255)
    
class RegionSi(models.Model):
    region_do = models.ForeignKey(RegionDo)
    name = models.CharField(max_length=255)

class Category(models.Model):
    name = models.CharField(max_length=255)
    
class Main(models.Model):
    name = models.CharField(max_length=255)
    category  = models.ForeignKey(Category)

class Sub(models.Model):
    name = models.CharField(max_length=255)
    main = models.ForeignKey(Main)

class SSub(models.Model):
    name = models.CharField(max_length=255)
    sub = models.ForeignKey(Sub)

class Item(models.Model):
    price_r = models.IntegerField('Retail Price', blank=True, null=True)
    price_w = models.IntegerField('Wholesale Price', blank=True, null=True)
    unit = models.CharField(max_length=255)
    price_date = models.DateField('Price Date')
    region = models.ForeignKey(RegionSi)
    category = models.ForeignKey(SSub)

class SUser(models.Model):
    iemi = models.CharField(max_length=255)
