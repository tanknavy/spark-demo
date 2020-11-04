package com.tanknavy.crm.app

import com.tanknavy.crm.view.CustomerView

object CustomerCrmApp {
  def main(args: Array[String]): Unit = {
    new CustomerView().mainMenu();
  }
}
