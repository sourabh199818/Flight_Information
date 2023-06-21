// Filter rows based on filters
function filterRows() {
  var airlineFilter = document.getElementById("filterAirline").value.toLowerCase();
  var dropdownFilter = document.getElementById("filterDropdown").value.toLowerCase();

  var rows = Array.from(document.querySelectorAll("tbody tr"));

  rows.forEach(function(row) {
    var airlineName = row.querySelector("td:nth-child(4)").textContent.toLowerCase();
    var stopover = row.querySelector("td:nth-child(3)").textContent.toLowerCase();
    var timeCategory = row.querySelector("td:nth-child(2)").textContent.toLowerCase();

    var airlineMatch = airlineName.includes(airlineFilter);
    var dropdownMatch = dropdownFilter === "" ||
      (dropdownFilter === "stopover1" && stopover === "true") ||
      (dropdownFilter === "stopover2" && stopover === "false") ||
      timeCategory.includes(dropdownFilter);

    if (airlineMatch && dropdownMatch) {
      row.style.display = "table-row";
    } else {
      row.style.display = "none";
    }
  });
}

// Handle filter change events
var filterInputs = document.querySelectorAll(".filter-input, .filter-dropdown");
filterInputs.forEach(function(input) {
  input.addEventListener("input", filterRows);
});
