import React, {Component} from 'react';
import './App.css';
import TilgungForm from "./components/TilgungForm/TilgungForm";
import { Route, BrowserRouter as Router } from "react-router-dom";

class App extends Component {
  render() {
    return (
        <Router>
          <Route exact path="/" component={TilgungForm} />
        </Router>
    );
  }
}
export default App;
