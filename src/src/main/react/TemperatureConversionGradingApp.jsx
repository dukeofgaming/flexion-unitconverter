import React from "react";
import ReactDOM from 'react-dom';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';

class TemperatureConversionGradingApp extends React.Component {
    constructor(props){
        super(props);

        this.grade = this.grade.bind(this);
    }

    grade(){
        const element   =  ReactDOM.findDOMNode(this);

        var input_value         = element.querySelector("input[name=input_value]").value;
        var input_unit          = element.querySelector("input[name=input_unit]").value;
        var target_unit         = element.querySelector("input[name=target_unit]").value;
        var student_response    = element.querySelector("input[name=student_response]").value;

        var output_element = element.querySelector("#output");

        if(
            input_value         === "" ||
            input_unit          === "" ||
            target_unit         === "" ||
            student_response    === ""
        ){
            output_element.innerHTML = '<i>Incomplete input</i>';
            console.log("Incomplete input, cancelling request to server");
            return;
        }


        var rounded_student_response    = Math.round(student_response*10)/10;

        console.log(
            "input_value=" + input_value
            + "&input_unit=" + input_unit
            + "&target_unit=" + target_unit
        );

        fetch(
            "http://localhost:8080/api/convert?"
            + "input_value=" + input_value
            + "&input_unit=" + input_unit
            + "&target_unit=" + target_unit
        ).then(
            response => response.text()
        ).then(
            authoritative_answer => {

                console.log("Authoritative answer: " + authoritative_answer + " (" + typeof authoritative_answer +")");
                console.log("Student (rounded): " + rounded_student_response + " (" + typeof rounded_student_response +")");

                output_element.innerHTML = "<b>Output:</b>&nbsp;" + (
                    (authoritative_answer === "invalid")?(
                        "invalid"
                    ):((parseFloat(authoritative_answer) === rounded_student_response))?(
                        "correct"
                    ):(
                        "incorrect"
                    )
                );

            }
        );
    }

    render() {
        return (
            <Box sx={{
                width: '450px'
            }}>
                <Paper
                    elevation={2}
                    sx={{
                        padding: '15px'
                }}>
                    <Grid container spacing={2} columns={2}>
                        <Grid item xs={2} sx={{
                            'font-weight': 'bolder',
                            'text-align': 'center'
                        }}>
                            Science Unit: Unit Converter
                        </Grid>
                        <Grid item xs={1}>
                            <TextField
                                variant="outlined"
                                label="Input Numerical Value"
                                name="input_value"
                                onBlur={this.grade}
                            />
                        </Grid>
                        <Grid item xs={1}>
                            <TextField
                                variant="outlined"
                                label="Input Unit of Measure"
                                name="input_unit"
                                onBlur={this.grade}
                            />
                        </Grid>
                        <Grid item xs={1}>
                            <TextField
                                variant="outlined"
                                label="Student Response"
                                name="student_response"
                                onBlur={this.grade}
                            />
                        </Grid>
                        <Grid item xs={1}>
                            <TextField
                                variant="outlined"
                                label="Target Unit of Measure"
                                name="target_unit"
                                onBlur={this.grade}
                            />
                        </Grid>
                        <Grid item xs={2}>
                            <Paper
                                id="output"
                                elevation={4}
                                sx={{
                                    padding: '15px',
                                    'text-transform': 'capitalize',
                                    'text-align': 'center'
                            }}>
                                <i>Please fill the form above</i>
                            </Paper>
                        </Grid>
                    </Grid>
                </Paper>
            </Box>
        );
    }
}

ReactDOM.render(
    <TemperatureConversionGradingApp />,
    document.getElementById('react-TemperatureConversionGradingApp')
);