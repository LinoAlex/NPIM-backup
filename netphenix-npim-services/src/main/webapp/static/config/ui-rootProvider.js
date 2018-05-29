/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


app.config(function($stateProvider){
  $stateProvider  
          .state("index",{
           url:"/index",
           templateUrl:"static/views/crudTable.html",
           controller:"tableController"       
  });
});



