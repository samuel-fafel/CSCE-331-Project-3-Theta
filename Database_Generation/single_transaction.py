#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Feb 28 11:39:07 2023

@author: samuelfafel

"""
import csv
import random

def single_random_transaction(ID, date, time):
    # Read in the meal menu
    meal_menu = {}
    with open('meal_menu.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) # skip header row
        for row in reader:
            meal_menu[row[0]] = float(row[6])
    
    # Read in the entree menu
    entree_menu = []
    with open('entree_menu.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) # skip header row
        for row in reader:
            entree_menu.append((row[0], float(row[6])))
    
    # Read in the sides menu
    sides_menu = []
    with open('sides_menu.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) # skip header row
        for row in reader:
            sides_menu.append((row[0], float(row[6])))
    
    # Read in the drink menu
    drink_menu = []
    with open('drink_menu.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader) # skip header row
        for row in reader:
            drink_menu.append((row[0], float(row[6])))
    
    payment_method = ["Card", "Dining Dollars"];
    payment_choice = random.choice(payment_method);
    
    cashiers = ["Nathan Coles", "Nick Nguyen", "Namson Pham", "Haden Johnson", "Samuel Fafel"]
    conductor = random.choice(cashiers)
    
    # Create the cart dictionary
    cart = {}
    
    # Choose a random meal
    meal_choice = random.choice(list(meal_menu.keys()))
    entree_list = ['none', 'none', 'none']
    sides_list = ['none', 'none']
    
    # Choose items based on the meal choice
    if meal_choice == 'Bowl':
        entree_choices = random.choice(entree_menu)
        cart[entree_choices[0]] = entree_choices[1]
        entree_list[0] = entree_choices[0]
        
        sides_choice = random.choice(sides_menu)
        cart[sides_choice[0]] = sides_choice[1]
        sides_list[0] = sides_choice[0]
        
    elif meal_choice == 'Plate':
        entree_choices = random.sample(entree_menu, k=2)
        for i, entree in enumerate(entree_choices):
            cart[entree[0]] = entree[1]
            entree_list[i] = entree[0]
            
        sides_choice = random.choice(sides_menu)
        cart[sides_choice[0]] = sides_choice[1]
        sides_list[0] = sides_choice[0]
        
    elif meal_choice == 'Bigger_Plate':
        entree_choices = random.sample(entree_menu, k=3)
        for i, entree in enumerate(entree_choices):
            cart[entree[0]] = entree[1]
            entree_list[i] = entree[0]
        
        sides_choice = random.choice(sides_menu)
        cart[sides_choice[0]] = sides_choice[1]
        sides_list[0] = sides_choice[0]
        
    elif meal_choice == 'Family_Meal':
        entree_choices = random.sample(entree_menu, k=3)
        for i, entree in enumerate(entree_choices):
            cart[entree[0]] = entree[1]
            entree_list[i] = entree[0]
          
        sides_choices = random.sample(sides_menu, k=2)
        for j, sides in enumerate(sides_choices):
            cart[sides[0]] = sides[1]
            sides_list[j] = sides[0]
            
    elif meal_choice.find("Entree"):
        entree_choices = random.choice(entree_menu);
        cart[entree_choices[0]] = entree_choices[1]
        entree_list[0] = entree_choices[0]
        
    elif meal_choice.find("Side"):
        sides_choice = random.choice(sides_menu);
        cart[sides_choice[0]] = sides_choice[1]
        sides_list[0] = sides_choice[0]
        
    
    # Choose a random drink
    drink_choice = random.choice(drink_menu)
    
    # Calculate subtotal, tax, and total
    subtotal = meal_menu[meal_choice] + drink_choice[1]
    tax = subtotal * 0.0825
    total = subtotal + tax        
    
    """
    # Print out receipt
    print("----------RECEIPT----------")
    print(f"{meal_choice}: ${meal_menu[meal_choice]:.2f}")
    for entree in entree_list:
        print(f"  Entree: {entree}")
    for side in sides_list:
        print(f"  Side: {side}")
    print(f"{drink_choice[0]}: ${drink_choice[3]:.2f}")
    print()
    print(f"Payment Method: {payment_choice}")
    print(f"Subtotal: ${subtotal:.2f}")
    print(f"Tax: ${tax:.2f}")
    print(f"Total: ${total:.2f}")
    """
    # Write to Transactions.csv
    with open('Transactions.csv', mode='a') as file:
        writer = csv.writer(file)
        writer.writerow([ID, 'Sale', meal_choice, entree_list[0], entree_list[1], entree_list[2], sides_list[0], sides_list[1], drink_choice[0], date, conductor, payment_choice, f'{subtotal:.2f}', f'{tax:.2f}', f'{total:.2f}', time])
    file.close()
    
    return subtotal
