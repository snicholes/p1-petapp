Feature: the available pets page works

Background: Given the user is on the available pets page

Scenario: the available pets show up in the table
	Given the user is on the available pets page
	When the available pets page is loaded
	Then the table has pets in it
	
Scenario: users can adopt pets
	Given the user is on the available pets page
	And the user is logged in
	When the available pets page is loaded
	And the user clicks the first adopt button
	Then the pet is removed from the table