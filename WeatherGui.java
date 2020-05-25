import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


/**
 * 
 * @author Eric Chun
 *
 */


public class WeatherGui {

	// - Fields
	private JFrame frame;   // - Swing components
	private JPanel introParentPanel, introChildPanel, introLabelPanel, fullPanel, currentPanel, hourlyHeadingPanel, hourlyPanel, dailyHeadingPanel, dailyPanel;
	private JLabel introLabel, cityLabel, stateLabel, currentTemp, currentWeatherDescription, currentTime, hourlyHeading, dailyHeading;
	private JTextField cityText, stateText;
	private JButton enter, location;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   // - Scale the GUI to the screensize.
	private int frameWidth = (int) screenSize.getWidth() / 2;
	private int frameHeight = (int) screenSize.getHeight( )* 2/3;
	private int borderThickness = frameWidth / 20;

	private String inputtedCity, inputtedState;   // - Use the inputted location to get weather data.


	private static Color darker;   // - Colors to match weather.

	private Color blue = new Color(70, 160, 235);
	private Color blueDark = new Color(50, 140, 215);

	private Color gray = new Color(110, 130, 140);
	private Color grayDark = new Color(90, 110, 120);

	private Color black = new Color (40, 50, 60);
	private Color blackDark = new Color(20, 30, 40);


	// - Create and show GUI
	private void createAndShowGUI() {
		frame = new JFrame("Weather");   // - Set up the empty frame.
		frame.getContentPane().setBackground(blue);
		frame.setSize(frameWidth, frameHeight);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		createIntroFrame();
	}


	// - Create intro/search frame
	private void createIntroFrame() {
		introParentPanel = new JPanel();   // - Parent covers full frame
		introParentPanel.setOpaque(false);
		introParentPanel.setLayout(new GridBagLayout());

		introLabelPanel = new JPanel();   // - Holds introLabel.
		introLabelPanel.setOpaque(false);
		introLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, borderThickness/2, 0));
		GridBagConstraints introLabelPanelConstraints = new GridBagConstraints();
		introLabelPanelConstraints.gridx = 0;
		introLabelPanelConstraints.gridy = 0;

		introLabel = new JLabel("Enter any location in the world", SwingConstants.LEFT);   // - Prompts user for location
		introLabel.setFont(new Font("Arial", Font.BOLD, 50));
		introLabel.setForeground(Color.white);
		GridBagConstraints introLabelConstraints = new GridBagConstraints();
		introLabelConstraints.gridx = 0;
		introLabelConstraints.gridy = 0;
		introLabelConstraints.gridwidth = 2;
		introLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		introLabelConstraints.insets = new Insets(0, 0, borderThickness/8, 0);
		introLabelPanel.add(introLabel, introLabelConstraints);

		introChildPanel = new JPanel();   // - Child is centered in parent
		introChildPanel.setBackground(Color.white);
		introChildPanel.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness, borderThickness*3/2, borderThickness));
		introChildPanel.setLayout(new GridBagLayout());
		GridBagConstraints introChildPanelConstraints = new GridBagConstraints();
		introChildPanelConstraints.gridx = 0;
		introChildPanelConstraints.gridy = 1;

		cityLabel = new JLabel("City: ", SwingConstants.LEFT);   // - "City: " label
		cityLabel.setFont(new Font("Arial", Font.PLAIN, 36));
		cityLabel.setForeground(new Color(110, 110, 110));
		GridBagConstraints cityLabelConstraints = new GridBagConstraints();
		cityLabelConstraints.gridx = 0;
		cityLabelConstraints.gridy = 1;
		cityLabelConstraints.gridwidth = 2;
		cityLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cityLabelConstraints.insets = new Insets(0, 0, borderThickness/8, 0);
		introChildPanel.add(cityLabel, cityLabelConstraints);

		cityText = new JTextField(12);   // - City input
		cityText.setFont(new Font("Arial", Font.PLAIN, 36));
		GridBagConstraints cityTextConstraints = new GridBagConstraints();
		cityTextConstraints.gridx = 0;
		cityTextConstraints.gridy = 2;
		cityTextConstraints.insets = new Insets(borderThickness/8, 0, borderThickness/3, 0);
		introChildPanel.add(cityText, cityTextConstraints);

		stateLabel = new JLabel("State/Country: ", SwingConstants.LEFT);   // - "State: " label
		stateLabel.setFont(new Font("Arial", Font.PLAIN, 36));
		stateLabel.setForeground(new Color(110, 110, 110));
		GridBagConstraints stateLabelConstraints = new GridBagConstraints();
		stateLabelConstraints.gridx = 0;
		stateLabelConstraints.gridy = 3;
		stateLabelConstraints.gridwidth = 2;
		stateLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		stateLabelConstraints.insets = new Insets(borderThickness/3, 0, borderThickness/8, 0);
		introChildPanel.add(stateLabel, stateLabelConstraints);

		stateText = new JTextField(12);   // - State input
		stateText.setFont(new Font("Arial", Font.PLAIN, 36));
		GridBagConstraints stateTextConstraints = new GridBagConstraints();
		stateTextConstraints.gridx = 0;
		stateTextConstraints.gridy = 4;
		stateTextConstraints.insets = new Insets(borderThickness/8, 0, borderThickness/4, 0);
		introChildPanel.add(stateText, stateTextConstraints);

		enter = new JButton("Search");   // - Search button
		enter.setFont(new Font("Arial", Font.PLAIN, 30));
		enter.setForeground(Color.white);
		enter.setBackground(new Color(40, 208, 55));
		enter.addActionListener(new ButtonActionListener());
		GridBagConstraints enterConstraints = new GridBagConstraints();
		enterConstraints.gridx = 0;
		enterConstraints.gridy = 5;
		enterConstraints.gridwidth = 2;
		enterConstraints.fill = GridBagConstraints.BOTH;
		enterConstraints.ipady = borderThickness/2;
		enterConstraints.insets = new Insets(borderThickness/2, 0, 0, 0);
		introChildPanel.add(enter, enterConstraints);
		frame.getRootPane().setDefaultButton(enter);   // - Enter key on the keyboard activates the button too.

		introParentPanel.add(introLabelPanel, introLabelPanelConstraints);   // - Search for the location and show the weather data.
		introParentPanel.add(introChildPanel, introChildPanelConstraints);
		frame.add(introParentPanel);
	}


	// - Create and add panels
	private void createAndAddPanels() {
		frame.remove(introParentPanel);

		fullPanel = new JPanel();   // - Set up fullPanel.		
		fullPanel.setOpaque(false);
		fullPanel.setSize(frameWidth, frameHeight);
		fullPanel.setLayout(new GridBagLayout());
		fullPanel.setBorder(BorderFactory.createEmptyBorder(borderThickness/2, borderThickness, borderThickness/2, borderThickness));

		currentPanel = new JPanel();   // - Set up currentPanel.
		currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
		currentPanel.setOpaque(false);
		GridBagConstraints currentConstraints = new GridBagConstraints();
		currentConstraints.gridx = 0;
		currentConstraints.gridy = 0;
		currentConstraints.weightx = 1.0;
		currentConstraints.fill = GridBagConstraints.BOTH;

		hourlyHeadingPanel = new JPanel();   // - Set up heading for hourlyPanel.
		hourlyHeadingPanel.setLayout(new BoxLayout(hourlyHeadingPanel, BoxLayout.Y_AXIS));
		hourlyHeadingPanel.setOpaque(false);
		GridBagConstraints hourlyHeadingConstraints = new GridBagConstraints();
		hourlyHeadingConstraints.gridx = 0;
		hourlyHeadingConstraints.gridy = 1;
		hourlyHeadingConstraints.weightx = 1.0;
		hourlyHeadingConstraints.fill = GridBagConstraints.BOTH;

		hourlyPanel = new JPanel();   // - Set up hourlyPanel.
		hourlyPanel.setLayout(new GridBagLayout());
		hourlyPanel.setOpaque(false);
		GridBagConstraints hourlyConstraints = new GridBagConstraints();
		hourlyConstraints.gridx = 0;
		hourlyConstraints.gridy = 2;
		hourlyConstraints.weightx = 1.0;
		hourlyConstraints.fill = GridBagConstraints.BOTH;

		dailyHeadingPanel = new JPanel();   // - Set up of heading for dailyPanel.
		dailyHeadingPanel.setLayout(new BoxLayout(dailyHeadingPanel, BoxLayout.Y_AXIS));
		dailyHeadingPanel.setOpaque(false);
		GridBagConstraints dailyHeadingConstraints = new GridBagConstraints();
		dailyHeadingConstraints.gridx = 0;
		dailyHeadingConstraints.gridy = 3;
		dailyHeadingConstraints.weightx = 1.0;
		dailyHeadingConstraints.fill = GridBagConstraints.BOTH;

		dailyPanel = new JPanel();   // - Set up dailyPanel.
		dailyPanel.setLayout(new GridBagLayout());
		dailyPanel.setOpaque(false);
		GridBagConstraints dailyConstraints = new GridBagConstraints();
		dailyConstraints.gridx = 0;
		dailyConstraints.gridy = 4;
		dailyConstraints.weightx = 1.0;
		dailyConstraints.fill = GridBagConstraints.BOTH;

		fullPanel.add(currentPanel, currentConstraints);   // - Add everything to fullPanel.
		fullPanel.add(hourlyHeadingPanel, hourlyHeadingConstraints);
		fullPanel.add(hourlyPanel, hourlyConstraints);
		fullPanel.add(dailyHeadingPanel, dailyHeadingConstraints);
		fullPanel.add(dailyPanel, dailyConstraints);
		frame.add(fullPanel);
	}


	// - Create and add labels
	private void createAndAddLabels() throws IOException {	
		WeatherData weatherData = new WeatherData();   // - Create instance of weatherData so we can use its methods and data for the labels.
		String[] coordinates = weatherData.getCoordinates(inputtedCity.replace(" ", ""), inputtedState.replace(" ", ""));   // - Use inputted location in MapQuest URL, get rid of spaces.
		weatherData.getWeatherData(coordinates[0], coordinates[1]);   // - Use coordinates from MapQuest API to get data from OpenWeather.

		location = new JButton("");   // - City and state
		location.setFont(new Font("Arial", Font.PLAIN, 30));
		location.setForeground(Color.white);
		location.setAlignmentX(Component.CENTER_ALIGNMENT);
		location.setText(cleanName(weatherData.getCity()) + ", " + cleanName(weatherData.getState()));
		location.addActionListener(new ButtonActionListener());
		location.addMouseListener(new MouseActionListener());

		currentTemp = new JLabel("", SwingConstants.CENTER);   // - Current temperature
		currentTemp.setFont(new Font("Arial", Font.PLAIN, 140));
		currentTemp.setForeground(Color.white);
		currentTemp.setBorder(BorderFactory.createEmptyBorder(-borderThickness/3, 0, -borderThickness/3, 0));   // - Remove empty space.
		currentTemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentTemp.setText(weatherData.getCurrentTemp() + "\u00B0");

		currentWeatherDescription = new JLabel("", SwingConstants.CENTER);   // - Current weather description
		currentWeatherDescription.setFont(new Font("Arial", Font.PLAIN, 30));
		currentWeatherDescription.setForeground(Color.white);
		currentWeatherDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentWeatherDescription.setText(weatherData.getCurrentWeatherDescription());

		currentTime = new JLabel("", SwingConstants.CENTER);   // - Current local time
		currentTime.setFont(new Font("Arial", Font.PLAIN, 16));
		currentTime.setForeground(Color.white);
		currentTime.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentTime.setText("Local time of update: " + weatherData.getCurrentTime());

		hourlyHeading = new JLabel("Hourly", SwingConstants.LEFT);   // - Hourly heading
		hourlyHeading.setFont(new Font("Arial", Font.PLAIN, 26));
		hourlyHeading.setForeground(Color.white);
		hourlyHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
		hourlyHeading.setBorder(BorderFactory.createEmptyBorder(borderThickness/6, borderThickness/6, borderThickness/4, borderThickness/6));
		hourlyHeadingPanel.add(hourlyHeading);   // - Add to hourlyPanel.

		for (int i = 0; i < 12; i++) {   // - Hourly labels
			JLabel hour = new JLabel(weatherData.getHourlyHours()[i], SwingConstants.CENTER);   // - Hour
			hour.setName("hourlyHour" + i);
			hour.setFont(new Font("Arial", Font.PLAIN, 22));
			hour.setForeground(Color.white);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			c.weightx = 1.0;
			c.ipady = borderThickness / 12;
			c.fill = GridBagConstraints.BOTH;
			hourlyPanel.add(hour, c);

			JLabel icon = new JLabel("", SwingConstants.CENTER);   // - Weather icon
			icon.setName("hourlyIcon" + i);
			icon.setFont(new Font("Arial", Font.PLAIN, 10));
			icon.setForeground(Color.white);
			String iconName = weatherData.getHourlyIcons()[i];
			InputStream stream = WeatherGui.class.getResourceAsStream("/Icons/" + iconName + ".png");
			ImageIcon imageIcon = new ImageIcon(ImageIO.read(stream));
			imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance((int) (borderThickness * 2.3), (int) (borderThickness * 2.3), Image.SCALE_DEFAULT));   // - Scale the icon
			icon.setIcon(imageIcon);
			c.gridy = 1;
			hourlyPanel.add(icon, c);

			JLabel temp = new JLabel(weatherData.getHourlyTemperatures()[i] + "\u00B0", SwingConstants.CENTER);   // - Temperature
			temp.setName("hourlyTemp" + i);
			temp.setFont(new Font("Arial", Font.PLAIN, 26));
			temp.setForeground(Color.white);
			c.gridy = 2;
			hourlyPanel.add(temp, c);
		}

		dailyHeading = new JLabel("Daily", SwingConstants.LEFT);   // - Daily Heading
		dailyHeading.setFont(new Font("Arial", Font.PLAIN, 26));
		dailyHeading.setForeground(Color.white);
		dailyHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
		dailyHeading.setBorder(BorderFactory.createEmptyBorder(borderThickness, borderThickness/6, borderThickness/6, borderThickness/6));
		dailyHeadingPanel.add(dailyHeading);   // - Add to dailyPanel

		for (int i = 0; i < 7; i++) {   // - Daily labels
			JLabel day = new JLabel(weatherData.getDailyDays()[i], SwingConstants.CENTER);   // - Day
			day.setName("dailyDay");
			day.setFont(new Font("Arial", Font.PLAIN, 24));
			day.setForeground(Color.white);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = i;
			c.gridy = 0;
			c.weightx = 1.0;
			c.ipady = borderThickness / 12;
			c.fill = GridBagConstraints.BOTH;
			dailyPanel.add(day, c);

			JLabel icon = new JLabel("", SwingConstants.CENTER);   // - Weather icon
			icon.setName("dailyIcon");
			icon.setFont(new Font("Arial", Font.PLAIN, 22));
			icon.setForeground(Color.white);
			String iconName = weatherData.getDailyIcons()[i];

			InputStream stream = WeatherGui.class.getResourceAsStream("/Icons/" + iconName + ".png");
			ImageIcon imageIcon = new ImageIcon(ImageIO.read(stream));
			imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance((int) (borderThickness * 3.3), (int) (borderThickness * 3.3), Image.SCALE_DEFAULT));   // - Scale the icon
			icon.setIcon(imageIcon);
			c.gridy = 1;
			dailyPanel.add(icon, c);

			JPanel dailyHighAndLowPanel = new JPanel();   // - High and low
			dailyHighAndLowPanel.setOpaque(false);
			dailyHighAndLowPanel.setLayout(new BoxLayout(dailyHighAndLowPanel, BoxLayout.X_AXIS));
			JLabel dailyHigh = new JLabel(weatherData.getDailyHighs()[i] + "\u00B0", SwingConstants.CENTER);
			dailyHigh.setName("dailyHigh");
			dailyHigh.setFont(new Font("Arial", Font.PLAIN, 30));
			dailyHigh.setForeground(Color.white);
			dailyHigh.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			JLabel dailyLow = new JLabel(weatherData.getDailyLows()[i] + "\u00B0", SwingConstants.CENTER);
			dailyLow.setName("dailyLow");
			dailyLow.setFont(new Font("Arial", Font.PLAIN, 22));
			dailyLow.setForeground(new Color(210, 210, 210));
			dailyLow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			dailyHighAndLowPanel.add(Box.createHorizontalGlue());
			dailyHighAndLowPanel.add(dailyHigh);
			dailyHighAndLowPanel.add(Box.createRigidArea(new Dimension(borderThickness/6, 0)));
			dailyHighAndLowPanel.add(dailyLow);
			dailyHighAndLowPanel.add(Box.createHorizontalGlue());
			c.gridy = 3;
			dailyPanel.add(dailyHighAndLowPanel, c);
		}

		String desc = weatherData.getCurrentWeatherDescription();
		setColorToWeather(desc);

		currentPanel.add(location);   // - Add to currentPanel.
		currentPanel.add(currentTemp);
		currentPanel.add(currentWeatherDescription);
		currentPanel.add(Box.createRigidArea(new Dimension(0, borderThickness/10)));
		currentPanel.add(currentTime);
	}


	// - Set frame color to weather
	private void setColorToWeather(String desc) {   // - Match the frame color to the current weather.

		ArrayList<String> blackSky = new ArrayList<String>();
		blackSky.add("Thunderstorm");
		blackSky.add("Heavy Intensity Rain");
		blackSky.add("Very Heavy Rain");
		blackSky.add("Extreme Rain");

		ArrayList<String> blueSky = new ArrayList<String>();
		blueSky.add("Clear");
		blueSky.add("Few");
		blueSky.add("Scattered");

		for (String s : blackSky) {
			if (desc.contains(s)) {
				frame.getContentPane().setBackground(black);
				location.setBackground(black);
				location.setBorder(BorderFactory.createLineBorder(blackDark, 5, true));
				darker = blackDark;
				return;
			}
		}

		for (String s : blueSky) {
			if (desc.contains(s)) {
				frame.getContentPane().setBackground(blue);
				location.setBackground(blue);
				location.setBorder(BorderFactory.createLineBorder(blueDark, 5, true));
				darker = blueDark;
				return;
			}
		}

		frame.getContentPane().setBackground(gray);   // - Set to gray if not black or blue.
		location.setBackground(gray);
		location.setBorder(BorderFactory.createLineBorder(grayDark, 5, true));
		darker = grayDark;
		return;
	}


	// - Button action listener
	private class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(enter) && !cityText.getText().equals("")) {   // - Search for location
				inputtedCity = cleanName(cityText.getText());
				inputtedState = cleanName(stateText.getText());

				createAndAddPanels();
				try {
					createAndAddLabels();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				frame.validate();
			} else if (e.getSource().equals(location)) {
				frame.remove(fullPanel);
				frame.getContentPane().setBackground(blue);
				createIntroFrame();
				frame.validate();
			}
		}
	}


	// - Mouse listener
	private class MouseActionListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {   // - Button gets darker color when mouse hovers over it.
			if (e.getSource().equals(location)) {
				location.setBackground(darker);
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {   // - Color goes back to normal.
			if (e.getSource().equals(location)) {
				location.setBackground(frame.getContentPane().getBackground());
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}


	// - Clean up name
	private String cleanName(String input) {   // - Clean the names of locations
		if (input.length() == 2 && Character.isUpperCase(input.charAt(0)) && Character.isUpperCase(input.charAt(1))) {   // - Leave state acronyms alone
			return input;
		} else if (input.length() > 1) {   // - Capitalize every word for country names
			if (input.contains(" ")) {
				return WeatherData.capitalizeEveryWord(input);
			} else {   // - Capitalize the first letter for one word.
				input = input.replace(" ", "").toLowerCase();
				char firstLetter = input.charAt(0);
				return input = Character.toUpperCase(firstLetter) + input.substring(1, input.length());
			}
		} else {
			return input;
		}
	}


	// - Main method
	public static void main(String[] args) {
		WeatherGui myWeatherGui = new WeatherGui();
		javax.swing.SwingUtilities.invokeLater(()->myWeatherGui.createAndShowGUI());
	}
}
