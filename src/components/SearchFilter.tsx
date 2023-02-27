import { useEffect, useState } from "react";
import Type from "../DTO/Type";
import useFetch from "../functions/useFetch";

const SearchFilter = () => {
    const types: Type[] = useFetch('http://localhost:8000/types') as Type[];


    return ( 
    <section className="filter">
        <h3>FILTER</h3>
        <TypesSection />
        <PublishYearSection />
        <Button />
    </section> 
    );
}
 

// types checkbox
const TypesSection = () => {
    const types: Type[] = useFetch('http://localhost:8000/types') as Type[];

    return (
        <section className="filter__types">
            <h4 className="filter__title">Types</h4>
            <ul>
                { types.map( type => (
                <p className="type" key={type.id}>
                    <input type="checkbox" name={type.name} id={type.name} value={type.name} />
                    <label htmlFor={type.name}>{type.name}</label>
                </p>
                ))}
            </ul>
        </section>
    )
}


// publish year from-to
const PublishYearSection = () => {
    // get date and determine restrictions min max
    return (
        <section className="filter__publishyear">
            <h4 className="filter__title">Publish year</h4>
            <label>From:</label>
            <input type="number" min="1999" max="2023" placeholder="1999"/>
            <label>To:</label>
            <input type="number" min="1999" max="2023" placeholder="2023"/> 
        </section>
    )
}

const Button = () => {
    // how to send data to backend - url/var1={type} var2={min} var3={max}
    return (
        <button className="btn btn--search">Search</button>
    )
}

export default SearchFilter;