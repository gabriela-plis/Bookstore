import React, { LegacyRef, useEffect } from "react";
import { useState } from "react";
import Book from "../../DTO/BookDTO";
import BookType from "../../DTO/BookTypeDTO";
import useFetch from "../../functions/useFetch";
import TextInput from "../../reusable-components/TextInput";
import { BOOKS_URL } from "../../constants/constants";

const AddBookPanel = () => {

    const types = useFetch<BookType>(BOOKS_URL + '/types');

    const [initialBook, setInitialBook] = useState<Book>({
        title: "",
        author: "",
        publishYear: 1980,
        canBeBorrow: true,
        availableAmount: 5,
        type: {
            id: 0,
            name: ""
        }
    });

    const [book, setBook] =  useState<Book>(initialBook);
    
    useEffect(() => {
        if (types.length !== 0) {

            setBook(prevState => (
                {
                    ...prevState,
                    type: types[0]
                }
            ))

        }
        
    },[types])

    const bookFormTextInput = [
        {title: "Title", value:book.title, variableName: "title", className: "title"},
        {title: "Author", value:book.author, variableName: "author", className: "author"}
    ]

    const bookFormSelectInput = [
        {title: "Type", variableName: "type", className: "type", options: types}
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
    
    const handleTypeChange = (e: eParameterType) => {
        const index = types.findLastIndex(type => type.name === e.target.value)!
    
        setBook(prevState => (
            {
                ...prevState,
                type: types[index]
            }
        )) 

    }

    const formRef: LegacyRef<HTMLFormElement> = React.createRef();

    const [error, setError] = useState(false);

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        fetch(BOOKS_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify(book)
            })
            .then(resp => {
                if (resp.ok) {
                    setError(false)
                    setBook(initialBook)
                    formRef.current?.reset()
                } else {
                    throw Error()
                }
            })
            .catch( error => setError(true))

    }

    const handleReset = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        setBook(initialBook);
    }

    return ( 
        <section className="add-form">
            <h2 className="add-form__title">Add Book</h2>
            <form className="form" ref={formRef} onSubmit={(e) => handleSubmit(e)}>
                {bookFormTextInput.map(attribute => (
                    <p key={attribute.className} className="form__fields">
                        <label htmlFor={attribute.className}>{attribute.title}</label>
                        <TextInput 
                            name={attribute.variableName} 
                            state={attribute.value} 
                            setState={(e) => handleChange(e)} 
                            isRequired 
                        />
                    </p>
                ))} 
                {bookFormSelectInput.map(attribute => (
                    <p key={attribute.className} className="form__fields">
                        <label htmlFor={attribute.className}>{attribute.title}</label>
                        <select 
                            name={attribute.variableName}
                            onChange={(e) => handleTypeChange(e)}
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
                            min={attribute.min}
                            max={attribute.max}
                            name={attribute.variableName}
                            required
                            value={attribute.value} 
                            onChange={(e) => handleChange(e)}
                        />   
                    </p>                 
                ))}             
                <div>
                    <p>Should be borrow from now: </p>
                    <input 
                        type="radio" 
                        name="canBeBorrow" 
                        value="true"
                        id="yes"
                        defaultChecked
                        onChange={(e) => handleChange(e)} 
                    />
                    <label htmlFor="yes" className="add-form__radio">Yes</label>
                    <input 
                        type="radio" 
                        name="canBeBorrow" 
                        value="false"
                        id="no" 
                        onChange={(e) => handleChange(e)}
                    />
                    <label htmlFor="no" className="add-form__radio">No</label>
                </div>
                {error && <p className="error-text error-text--bold error-text--red">Something goes wrong, try again</p>}
                <p className="btns-container">
                <button className="btn btn--add" >Add</button> 
                <button className="btn btn--reset" onClick={(e) => handleReset(e)} type="reset">Reset</button>
                </p>
            </form>
            <span className="add-form__image"><div></div></span>
        </section>
        
    );
}

 
export default AddBookPanel;