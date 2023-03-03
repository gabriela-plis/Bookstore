import React from "react";
import { useState } from "react";
import ReactSelect from "react-select";
import Book from "../../DTO/Book";
import BookType from "../../DTO/Type";
import useFetch from "../../functions/useFetch";
import TextInput from "../../reusable-components/TextInput";

const AddBookPanel = () => {

    const initialStateOfBook: Book = {
        title: "",
        author: "",
        publishYear: 0,
        availableAmount: 0,
        type: ""
    }

    const [book, setBook] =  useState<Book>(initialStateOfBook)

    const types: BookType[] = useFetch('http://localhost:8000/types');


    const bookFormTextInput = [
        {title: "Title", value:book.title, variableName: "title", className: "title"},
        {title: "Author", value:book.author, variableName: "author", className: "author"}
    ]

    const bookFormSelectInput = [
        {title: "Type", value:book.title, variableName: "type", className: "type", options: types}
    ]

    const bookFormNumberInput = [
        {title: "Publish year", value:book.publishYear, variableName: "publishYear", className: "publish-year", min: "1980", max:"2023"},
        {title: "Available amount", value:book.availableAmount, variableName: "availableAmount", className: "available-amount", min:"5", max: "30"}
    ]

    type eParameterType = React.ChangeEvent<HTMLInputElement>|React.ChangeEvent<HTMLSelectElement>;
    const handleChange = (e: eParameterType) => {

        setBook(prevState => (
            {
                ...prevState,
                [e.target.name]: e.target.value
            }
        )) 

    }


    const handleSubmit = () => {
        
    }

    const handleReset = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();

        setBook(initialStateOfBook);
    }

    return ( 
        <section className="add-form">
            <h2 className="add-form__title">Add Book</h2>
            <form className="form" onSubmit={handleSubmit}>
                {bookFormTextInput.map(attribute => (
                    <p key={attribute.className} className="form__fields">
                        <label htmlFor={attribute.className}>{attribute.title}</label>
                        <TextInput 
                            name={attribute.variableName} 
                            state={attribute.value} 
                            setState={handleChange} 
                            isRequired 
                        />
                    </p>
                ))} 
                {bookFormSelectInput.map(attribute => (
                    <p key={attribute.className} className="form__fields">
                        <label htmlFor={attribute.className}>{attribute.title}</label>
                        <select 
                            name={attribute.variableName}
                            onChange={(e) => handleChange(e)}
                        >
                            {attribute.options.map(option => (
                                <option 
                                    key={option.id}
                                    value={option.name}
                                >
                                    {option.name}
                                </option>
                            ))}
                        </select>
                    </p>
                ))}   
                {bookFormNumberInput.map(attribute => (
                    <p key={attribute.className} className="form__fields">
                        <label htmlFor={attribute.className}>{attribute.title}</label>    
                        <input 
                            type="number"
                            name={attribute.variableName}
                            required
                            value={attribute.value} 
                            onChange={(e) => {handleChange(e)}}
                        />   
                    </p>                 
                ))} 
                <p className="btns-container">
                <button className="btn btn--add">Add</button> 
                <button className="btn btn--reset" onClick={(e) => handleReset(e)}>Reset</button> 
                </p>
            </form>
            <span className="add-form__image"><div></div></span>
        </section>
        
    );
}

 
export default AddBookPanel;