import React from 'react';
import './App.css';
import Header from './Header';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './Home';

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
        <div className="content">
            <Routes>
              <Route path="/" element={<Home />}></Route>
              {/* <Route path="/"></Route>
              <Route path="/"></Route> */}
            </Routes>
        </div>
      </Router>
    </div>
  );
}

export default App;
