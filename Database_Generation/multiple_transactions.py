#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Feb 28 13:21:13 2023

@author: samuelfafel
"""

import csv
import random
import datetime
from single_transaction import single_random_transaction

with open('Transactions.csv', mode='w') as file:
    writer = csv.writer(file)
    writer.writerow(['id', 'order_type', 'meal_size', 
                     'entree_1', 'entree_2', 'entree_3', 
                     'side_1', 'side_2', 'drink', 
                     'date', 'conducted_by', 'payment_method', 
                     'subtotal', 'tax', 'total', 'time'])
#"""
start_time = datetime.datetime(2022, 1, 17, 11, 00, 00) # Opening
end_time = datetime.datetime(2022, 1, 17, 21, 00, 00) # Closing
delta = datetime.timedelta(days=1)
delta_minutes = datetime.timedelta(minutes=1)
ID = 0
total_income = 0



# Spring 2022
start_date = datetime.datetime(2022, 1, 17, 11, 00, 00) # Spring Semester Start (01/17/2022_11:00:00)
end_date = datetime.datetime(2022, 5, 16, 22, 00, 00) # Spring Semester End (05/16/2022_22:00:00)
current_date = start_date
while current_date <= end_date:
    current_time = current_date
    transactions_per_day = random.randint(300, 400)# number of transactions/day
    if current_date.isocalendar()[1] != 11: # Spring Break
        for transaction in range(transactions_per_day):
            ID += 1;
            total_income += single_random_transaction( ID, current_date.strftime('%m/%d/%Y'), current_time.strftime("%H:%M:%S") )
            current_time += delta_minutes * 1.65
    current_date += delta


# Fall 2022
start_date = datetime.datetime(2022, 8, 23, 11, 00, 00) # Fall Semester Start
end_date = datetime.datetime(2022, 12, 15, 22, 00, 00) # Fall Semester End
current_date = start_date
gameday = False
while current_date <= end_date:
    current_time = current_date
    transactions_per_day = random.randint(300, 400)
    if current_date == datetime.date(2022, 9, 3) or current_date == datetime.date(2022, 11, 26):
        transactions_per_day = random.randint(600, 700) # <-- GAME DAY!
        gameday = True
    for transaction in range(transactions_per_day):
        ID += 1;
        total_income += single_random_transaction( ID, current_date.strftime('%m/%d/%Y'), current_time.strftime("%H:%M:%S") )
        if gameday:
            current_time += delta_minutes * 0.94
        else:
            current_time += delta_minutes * 1.65
    current_date += delta

# Spring 2023 (so far)
start_date = datetime.datetime(2023, 1, 17, 11, 00, 00) # Spring Semester Start
end_date = datetime.datetime(2023, 3, 17, 22, 00, 00) # Today
current_date = start_date
while current_date <= end_date:
    current_time = current_date
    transactions_per_day = random.randint(300, 400)
    for transaction in range(transactions_per_day):
        ID += 1;
        total_income += single_random_transaction( ID, current_date.strftime('%m/%d/%Y'), current_time.strftime("%H:%M:%S") )
        current_time += delta_minutes * 1.65
    current_date += delta

print(f'Number of Transactions: {ID}')
print(f'Total Income: {total_income:.2f}')

"""
single_random_transaction(0, "00/00/0000", "01:23:45")
#"""