/**
 * Azioni da eseguire sulla view
 */

function filterList() {
  //Dichiaro le variabili
  var input, filter, ul, li, a, i, txtValue;
  input = document.getElementById('myInput');
  filter = input.value.toUpperCase();
  ul = document.getElementById("myCustomers");
  li = ul.getElementsByTagName('li');

  //Scorro tutti gli elementi e nascondo quelli che non matchano la condizione
  for (i = 0; i < li.length; i++) {
    a = li[i].getElementsByTagName("a")[0];
    txtValue = a.textContent || a.innerText;
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      li[i].style.display = "";
    } else {
      li[i].style.display = "none";
    }
  }
}