import React from 'react';
import PropTypes from 'prop-types';
import CardText from "reactstrap/lib/CardText";
import CardBody from "reactstrap/lib/CardBody";
import DataTable from "react-data-table-component";
import {formatCurrency} from "../../utils/currencyFormat";

const TilgungPlan = (props) => {

  const data = props.planData.yearlyBezahlungDTOS;
  const columns = [
    {
      name: 'Jahr',
      selector: 'year',
      sortable: true,
    },
    {
      name: 'Jährliche Rate',
      selector: 'rate',
      sortable: true,
      cell: (yearly) => formatCurrency(yearly.rate)
    },
    {
      name: 'Jährliche Zinsen',
      selector: 'zinsen',
      sortable: true,
      cell: (yearly) => formatCurrency(yearly.zinsen)
    },
    {
      name: 'Jährliche Tilgung',
      selector: 'tilgung',
      sortable: true,
      cell: (yearly) => formatCurrency(yearly.tilgung)
    },
    {
      name: 'Restschuld',
      selector: 'restSchuld',
      sortable: true,
      cell: (yearly) => formatCurrency(yearly.restSchuld)
    },
  ];

  const customStyles = {
    headRow: {
      style: {
        border: 'solid',
        borderWidth: '1px',
        background: 'ghostwhite',
      },
    },
    headCells: {
      style: {
        color: '#202124',
        fontSize: '1em',
      },
    },
    rows: {
      highlightOnHoverStyle: {
        backgroundColor: 'rgb(230, 244, 244)',
        borderBottomColor: '#FFFFFF',
        borderRadius: '25px',
        outline: '1px solid #FFFFFF',
      },
      style: {
        fontSize: '0.93em',
      },
    },
    pagination: {
      style: {
        border: 'solid',
        borderWidth: "1px"
      },
    },
  };

  return (
      <div>
        <CardBody style={{
          textAlign: "center",
          background: "ghostwhite",
          color: "dimgray",
          marginTop: "5px",
          fontSize: "1.20em",
          fontWeight: "bold",
          border: 'solid',
          borderWidth: '1px',
        }}>
          <CardText style={{float: "center"}}>
            Monatsrate: {formatCurrency(props.planData.monthlyRate, 2)}
          </CardText>
          {props.planData.restSchuld !== 0 ?
              (
                  <CardText style={{float: "center"}}>
                    Restschuld: {formatCurrency(props.planData.restSchuld, 2)}
                  </CardText>
              ) : (<CardText style={{float: "center"}}>
                Bereits innerhalb der Zinsbindungsdauer Vollgetilgt.
              </CardText>)
          }
        </CardBody>
        <div>
          <DataTable
              id="tilgungsplan"
              title="Tilgungsplan"
              columns={columns}
              data={data}
              customStyles={customStyles}
              pagination
          />
        </div>
      </div>
  );
};

TilgungPlan.propTypes = {
  planData: PropTypes.shape({
    monthlyRate: PropTypes.number.isRequired,
    restSchuld: PropTypes.number.isRequired,
    yearlyBezahlungDTOS: PropTypes.arrayOf(PropTypes.shape({
      rate: PropTypes.number.isRequired,
      restSchuld: PropTypes.number.isRequired,
      tilgung: PropTypes.number.isRequired,
      year: PropTypes.string.isRequired,
      zinsen: PropTypes.number.isRequired
    }).isRequired).isRequired
  }).isRequired
};

TilgungPlan.defaultProps = {
  planData: {
    monthlyRate: 0,
    restSchuld: 0,
    yearlyBezahlungDTOS: []
  }
};

export default TilgungPlan;
