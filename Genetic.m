% To be clear.. this is not a path finding problem. This is the Travelling Salesman 
% problem (http://goo.gl/BsbW). In this problem a salesman starts at a given town
% and then has to visit a list of other towns (in any order). The aim is to find the
% best order in which to visit towns so that he minimizes travel distance.
% In Kwok's variation he ends in a specified town instead of returning home which is
% the traditional problem.

function mainGenetic()
    global start;
    global starttemp;
    global dest;
    global desttemp;
    global towns;
    global townstemp;

    % --------- START CONFIG ----------

    % Problem Parameters
    FIELD = [-56 56 -46 46];
    TOWNS_N = 15;
    % Genetic Algorithm Parameters
    POPULATION_VALUE = 200;
    GENERATIONS_N = 500;
    MUTATE_P = 0.1;
    CROSSOVER_P = 0.5;
    
    % --------- END CONFIG ----------
    
    % We assume population size is even. Guarantee this.
    modulate_population = mod(POPULATION_VALUE,2);
    POPULATION_VALUE = POPULATION_VALUE + modulate_population;
    
    % Generate random towns to visit
    town_update = TOWNS_N+2
    points_random = rand(town_update,2);
    
    pointsA = points_random(:,1).*(FIELD(2)-FIELD(1));
    pointsB = points_random(:,2).*(FIELD(4)-FIELD(3));
    pointsRangeL = pointsA+FIELD(1);
    pointsRangeH = pointsB+FIELD(3);
    
    points_random = [pointsRangeL, pointsRangeH];
    
    start = points_random(1,:);
    starttemp = points_random(1,:);
    dest = points_random(end,:);
    desttemp = points_random(end,:);
    towns = points_random(2:end-1,:);
    townstemp = points_random(2:end-1,:);
    inttemp = 0;
    % Generate Population by randomly shuffling the towns
    my_population = zeros(POPULATION_VALUE, TOWNS_N);
    if (inttemp == 0)
        for i=1:POPULATION_VALUE
            my_population(i,:) = randperm(TOWNS_N);
        end
    end
    % Create Plot
    figure(1); clf;
    points1 = points_random(:,1);
    points2 = points_random(:,2);
    plot(points1, points2, 'r*');
    myString = num2str([1:TOWNS_N]')
    labels = ['Start';cellstr( myString );'End'];
    text(points_random(:,1), points_random(:,2), labels, 'VerticalAlignment','bottom', 'HorizontalAlignment','right');
    axis([-60 60 -50 50]);
    pathH = [];

    % Simulation Loop
    for generation=1:GENERATIONS_N
        flag1 = 0;
        flag2 = 0;
        flag3 = 0;
        % Evaluate Fitness (better should be larger)
        perf = performance(my_population);
        perf_diff = max(perf)-perf;
        perf_diff_square = perf_diff.^2;
        fitness = perf_diff_square;
        
        % Cumulative fitness is normalised so we have a distribution 
        % to help select the intermediate population.
        cumulative_fitness = cumsum(fitness);
        cum_fit_divider = max(cumulative_fitness);
        cumulative_fitness = cumulative_fitness/cum_fit_divider;

        % Selection
        
        if (flag1 == 0)
            zeros_input_pop = POPULATION_VALUE;
            zeros_input_towns = TOWNS_N
            temp_population = zeros(zeros_input_pop, zeros_input_towns);
            
            for j=1:POPULATION_VALUE
                temp_population(j,:) = choose_temp_pop(my_population, cumulative_fitness);
            end
        end
        
        % Recombination / Crossover
            % Note: We are performing crossover on an Ordered Chromosome. This means
            %       we will likely produce errors that need to be repaired. There are
            %       alternate methods to avoid this: http://goo.gl/Wql0OL
        new_population = zeros(POPULATION_VALUE, TOWNS_N);
        if (flag2 == 0)
            for j=2:2:POPULATION_VALUE
                % Select two parents
                parent_1 = temp_population(j-1,:);
                parent_2 = temp_population(j,:);
                % With probability P_CROSSOVER
                if rand() < CROSSOVER_P
                    % Perform 2-Point crossover. I have chosen this over one point crossover
                    %   as it has a better chance of unwinding loops and swapping sections
                    %   given the nature of the problem and chromosome encoding.
                    cp = sort(randi(TOWNS_N-1,2,1)); % Crossover Points
                    child_1 = [parent_1(1:cp(1)) parent_2(cp(1)+1:cp(2)) parent_1(cp(2)+1:end)];
                    child_2 = [parent_2(1:cp(1)) parent_1(cp(1)+1:cp(2)) parent_2(cp(2)+1:end)];
                    % Repair the faulty children and push them into the new population
                    new_population(j-1,:) = repair(child_1);
                    new_population(j,:) = repair(child_2);
                else
                    % Carry parents over to new population without crossover
                    new_population(j-1,:) = parent_1;
                    new_population(j,:) = parent_2;
                end
            end
        end
        % Mutation
        if (flag3 == 0)
            for j=2:POPULATION_VALUE
                % For each parameter in the chromosome string...
                for i=1:TOWNS_N
                    % ...with probability P_MUTATE...
                    if rand() < MUTATE_P
                        % ...pick another random parameter and swap them
                        k = randi(TOWNS_N,1);
                        new_population(j,[i k]) = new_population(j,[k i]);
                    end
                end
            end
        end
        % Elitism - copy best parent to new population
        best = my_population(find(perf == min(perf),1,'first'),:);
        new_population(1,:) = best;
        my_population = new_population;
        
        % Update Plot
        if (ishandle(pathH))
            delete(pathH);
        end
        hold on;
        path = [start; towns(best,:); dest];
        pathH = plot(path(:,1),path(:,2), 'b-');
        title(sprintf('Generation %d', generation));
        drawnow;
    end
    display(sprintf('Best path: %s\n', sprintf('%d ', best)));
end

% Repair a faulty chromosome
function c = repair(c)
    n = length(c);
    % Create a list of missing towns
    missing = setdiff(1:n,c);
    for i=randperm(length(c)) %Go through in random order
        % If there is duplicate town, replace it with a missing town
        if length(find(c == c(i))) > 1
            c(i) = missing(end);
            missing = missing(1:end-1);
        end
    end
end

% Select a chromosome for the intermediate population
%   "Roulette Wheel" method: Uses the cumulative fitness function to increase the
%   probability of chromosomes with higher fitness being included.
function c = choose_temp_pop(population, cum_fit)
    if isnan(cum_fit(1)) % No fitness when all chromosones equal. Pick randomly.
        c = population(randi(length(cum_fit),1),:);
    else
        i = find(cum_fit >= rand(),1,'first');
        c = population(i,:);
    end
end

% Calculate the performance of each chromosome.
%   "Performance" is considered the total distance travelled. So a lower performance is
%   better. Fitness is calculated using this such that higher fitness is better.
function sum = performance(chromosome)
    global start;
    global dest;
    global towns;
    sum = zeros(size(chromosome,1),1);
    for j=1:size(chromosome,1)
        sum(j) = dist(start, towns(chromosome(j,1),:)) + dist(towns(chromosome(j,end),:),dest);
        for i=2:size(chromosome,2)
            sum(j) = sum(j) + dist(towns(chromosome(j,i-1),:), towns(chromosome(j,i),:));
        end
    end
    
    % Internal function - pythagorean distance.
    function d = dist(A, B)
        d = sqrt((A(1)-B(1))^2 + (A(2)-B(2))^2);
    end
end
