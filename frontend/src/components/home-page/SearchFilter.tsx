import { useEffect, useState } from "react";
import BookType from "../../DTO/BookTypeDTO";
import useFetch from "../../functions/useFetch";

const SearchFilter = () => {

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
    const types: BookType[] = useFetch('http://localhost:8080/books/types');

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
    const currentYear = new Date().getFullYear()

    return (
        <section className="filter__publishyear">
            <h4 className="filter__title">Publish year</h4>
            <div className="filter__input-container">
                <label>From:</label>
                <input type="number" min="1999" max={currentYear} placeholder="1999"/>
                <label>To:</label>
                <input type="number" min="1999" max={currentYear} placeholder={currentYear.toString()}/> 
            </div>
        </section>
    )
}

const Button = () => {
    // how to send data to backend - url/var1={type} var2={min} var3={max}

    return (
        <section className="filter__button-container">
            <button className="btn btn--smaller">Search</button>
        </section>
    )
}

export default SearchFilter;