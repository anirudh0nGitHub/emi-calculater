import React, {useState} from "react";
import {makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import classNames from "classnames";
import Container from "@material-ui/core/Container";
import Col from "reactstrap/lib/Col";
import Label from "reactstrap/lib/Label";
import NumberFormat from 'react-number-format';
import InputGroup from "reactstrap/lib/InputGroup";
import TilgungPlan from "../TilgungPlan/TilgungPlan";
import {parseToStandardDecimal} from "../../utils/currencyFormat";
import PropTypes from 'prop-types';
import Row from "reactstrap/lib/Row";
import NavbarText from "reactstrap/lib/NavbarText";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    marginBottom: theme.spacing(1)
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));

const TilgungForm = (className) => {
  const classes = useStyles();
  const [formErrors] = useState({
    tilgungsatz: '',
    darlehensbetrag: '',
    sollzins: ''
  });
  const [planLoaded, setPlanLoaded] = useState(false);
  const [planData, setPlanData] = useState({
    monthlyRate: 0,
    restSchuld: 0,
    yearlyBezahlungDTOS: []
  });

  const [formValues, setFormValues] = useState({
    darlehensbetrag: '',
    sollzins: '',
    tilgungsatz: '',
    zinsbildung: ''
  });

  const nulCheck = RegExp('^€ 0*$');

  async function fetchYearlyPlan(payload) {
    const response = await fetch("http://localhost:8080/api/plan/yearly", {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json"
      },
      redirect: "follow",
      referrerPolicy: "no-referrer",
      body: JSON.stringify(payload)
    });
    let body = await response.json();
    setPlanData(body);
  }

  function isValidForm(payload) {
    return !(payload.darlehensbetrag === "" || nulCheck.test(payload.darlehensbetrag)
        || (parseToStandardDecimal(payload.darlehensbetrag) > 1000000000)
        || payload.tilgungsatz === "" || payload.tilgungsatz === "0"
        || (parseToStandardDecimal(payload.tilgungsatz) > 100)
        || (parseToStandardDecimal(payload.sollzins) > 100)
        || payload.sollzins === "");
  }

  function handleFormErrors(newValues) {
    if (newValues.tilgungsatz === '') {
      formErrors.tilgungsatz = "ist ein Pflichtfeld.";
    } else if (newValues.tilgungsatz === '0' || (parseFloat(newValues.tilgungsatz) > 100)) {
      formErrors.tilgungsatz = " darf nicht null oder größer als eine"
          + " Hundert sein.";
    } else {
      formErrors.tilgungsatz = "";
    }
    if (newValues.darlehensbetrag === '') {
      formErrors.darlehensbetrag = "ist ein Pflichtfeld.";
    } else if (nulCheck.test(newValues.darlehensbetrag) || (parseToStandardDecimal(newValues.darlehensbetrag) > 1000000000)) {
      formErrors.darlehensbetrag = " darf nicht null oder größer als eine"
          + " Billion"
          + " sein.";
    } else {
      formErrors.darlehensbetrag = "";
    }
    if (newValues.sollzins === '') {
      formErrors.sollzins = "ist ein Pflichtfeld.";
    } else if (parseToStandardDecimal(newValues.sollzins) > 100) {
      formErrors.sollzins = " darf nicht null oder größer als eine"
          + " Hundert sein.";
    } else {
      formErrors.sollzins = "";
    }
  }

  const handleInputChange = e => {
    const {name, value} = e.target;
    const newValues = {...formValues, [name]: value};

    handleFormErrors(newValues);
    setFormValues(newValues);

    if (planLoaded && isValidForm(newValues)) {
      fetchYearlyPlan(buildPayload(newValues));
    }
  };

  const handleSubmit = e => {
    const {name, value} = e.target;
    const newValues = {...formValues, [name]: value};
    setFormValues(newValues);

    handleFormErrors(newValues);
    if (isValidForm(newValues)) {
      fetchYearlyPlan(buildPayload(newValues));
      setPlanLoaded(true);
    }
    e.preventDefault();
  };

  const {
    darlehensbetrag,
    sollzins,
    tilgungsatz,
    zinsbildung
  } = formValues;

  function buildPayload(values) {
    return {
      darlehensbetrag: parseToStandardDecimal(values.darlehensbetrag),
      sollzins: parseToStandardDecimal(values.sollzins),
      tilgungsatz: parseToStandardDecimal(values.tilgungsatz),
      zinsbildung: parseToStandardDecimal(values.zinsbildung)
    }
  }

  return (
      <Container>
        <div className={classes.root}>
          <AppBar position="static">
            <Toolbar id="bar" style={{background: 'red'}}>
              <IconButton edge="start" className={classes.menuButton}
                          color="inherit" aria-label="menu">
                <MenuIcon/>
              </IconButton>
              <Typography variant="h6" className={classes.title}>
                Tilgungrechner
              </Typography>
              <NavbarText>Created by Anirudh</NavbarText>
            </Toolbar>
          </AppBar>
        </div>
        <Row>
          <Col md="12">
            <form onSubmit={handleSubmit} className={className}>
              <div className="grey-text">
                <InputGroup>
                  <Label>Darlehensbetrag:&nbsp;</Label><span
                    style={{color: 'red'}}>  {formErrors.darlehensbetrag} </span>
                  <NumberFormat icon="envelope" error="wrong"
                                allowNegative={false}
                                thousandSeparator={'.'} prefix={'€ '}
                                decimalSeparator={','}
                                className={classNames(
                                    {error: formErrors.darlehensbetrag})}
                                value={darlehensbetrag}
                                onChange={handleInputChange}
                                id="darlehensbetrag"
                                name="darlehensbetrag"
                                success="right"
                                style={{width: 'inherit'}}>
                  </NumberFormat>
                </InputGroup><br/>

                <InputGroup>
                  <Label>Sollzinssatz:&nbsp;</Label><span
                    style={{color: 'red'}}>  {formErrors.sollzins} </span>
                  <NumberFormat icon="envelope" error="wrong"
                                allowNegative={false}
                                thousandSeparator={'.'} decimalSeparator={','}
                                className={classNames(
                                    {error: formErrors.sollzins})}
                                value={sollzins}
                                onChange={handleInputChange}
                                id="sollzins"
                                name="sollzins"
                                success="right"
                                style={{width: 'inherit'}}>
                  </NumberFormat>
                </InputGroup><br/>

                <InputGroup>
                  <Label>Anfängliche Tilgung (%):&nbsp;</Label><span
                    style={{color: 'red'}}>  {formErrors.tilgungsatz} </span>
                  <NumberFormat icon="envelope" error="wrong"
                                allowNegative={false}
                                thousandSeparator={'.'} decimalSeparator={','}
                                className={classNames(
                                    {error: formErrors.tilgungsatz})}
                                value={tilgungsatz}
                                onChange={handleInputChange}
                                id="tilgungsatz"
                                name="tilgungsatz"
                                success="right"
                                style={{width: 'inherit'}}>
                  </NumberFormat>
                </InputGroup>
                <Label
                    style={{marginTop: 24}}>Zinsbindungsdauer(Optional):&nbsp;</Label>
                <select className="browser-default custom-select"
                        value={zinsbildung}
                        onChange={handleInputChange}
                        id="zinsbildung"
                        name="zinsbildung">
                  <option>Default Wert bis Ende 50 Jahren.
                  </option>
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                  <option value="6">6</option>
                  <option value="7">7</option>
                  <option value="8">8</option>
                  <option value="9">9</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
                  <option value="13">13</option>
                  <option value="14">14</option>
                  <option value="15">15</option>
                  <option value="16">16</option>
                  <option value="17">17</option>
                  <option value="18">18</option>
                  <option value="19">19</option>
                  <option value="20">20</option>
                  <option value="21">21</option>
                  <option value="22">22</option>
                  <option value="23">23</option>
                  <option value="24">24</option>
                  <option value="25">25</option>
                  <option value="26">26</option>
                  <option value="27">27</option>
                  <option value="28">28</option>
                  <option value="29">29</option>
                  <option value="30">30</option>
                </select>
              </div>
              <div className="text-center">
                <button type="button" style={{background: 'red', marginTop: 16}}
                        onClick={handleSubmit}
                        id="berechnen"
                        className="btn btn-default btn-lg btn-block text-white">
                  Berechnen
                </button>
              </div>
            </form>
          </Col>
          <Col>
            {!planLoaded ? (
                <br/>
            ) : (
                <TilgungPlan planData={planData}/>
            )}
          </Col>
        </Row>
      </Container>
  );
};

TilgungForm.propTypes = {
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

TilgungForm.defaultProps = {
  planData: {
    monthlyRate: 0,
    restSchuld: 0,
    yearlyBezahlungDTOS: []
  }
};

export default TilgungForm;
