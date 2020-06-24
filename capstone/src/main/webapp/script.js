async function associationUpdateDisplay() {
  let response = await fetch('/associations');
  let associations = await response.json();
  
  let positive = document.getElementById('pos-associations');
  positive.innerHTML = '';
  associations.positive.forEach(elem => addListElement(positive, elem));

  let negative = document.getElementById('neg-associations');
  negative.innerHTML = '';
  associations.negative.forEach(elem => addListElement(negative, elem));
}

function addListElement(list, contents) {
  const elem = document.createElement('li');
  elem.textContent = contents;
  list.appendChild(elem);
}

window.addEventListener('load', associationUpdateDisplay)
