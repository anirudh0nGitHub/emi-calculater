export function formatCurrency(value, digits = 2) {
  return new Intl.NumberFormat("de-DE", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: digits,
    maximumFractionDigits: digits
  }).format(value);
}


export function formatNumer(value, digits = 2) {
  return new Intl.NumberFormat("de-DE", {
    style: "decimal"
  }).format(parseInt(value).toLocaleString("de-DE"));
}

export function parseToStandardDecimal(toBeParsed) {
  return parseFloat(toBeParsed.toString()
  .replace(/\./g, '')
  .replace(/,/g, '.')
  .replace('€', ''));
}
