/**
 * Azioni da eseguire sulla view
 */

function filterList() {
  //Dichiaro le variabili
  var input, filter, ul, li, a, i, txtValue;
  
  input = document.getElementById('myInput'); //prendo l'oggetto di input
  filter = input.value.toUpperCase(); //prendo il testo inserito e faccio uppercase
  ul = document.getElementById("myCustomers"); //prendo la lista
  li = ul.getElementsByTagName('li'); //prendo tutti i list item

  //Scorro tutti gli elementi e nascondo quelli che non matchano la condizione
  for (i = 0; i < li.length; i++) {
    a = li[i].getElementsByTagName("a")[0];
    txtValue = a.textContent || a.innerText;
    
    //se il testo inserito occorre dentro il testo (flag di ritorno > 1) allora lo mostro
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      li[i].style.display = ""; 
    } else {
      //altrimenti lo nascondo
      li[i].style.display = "none";
    }
  }
}